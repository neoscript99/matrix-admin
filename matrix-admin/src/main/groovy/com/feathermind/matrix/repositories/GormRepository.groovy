package com.feathermind.matrix.repositories

import com.feathermind.matrix.util.JsonUtil
import com.feathermind.matrix.util.MatrixException
import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode
import groovy.util.logging.Slf4j
import com.feathermind.matrix.util.GormCriteriaUtil
import org.grails.datastore.gorm.GormEnhancer
import org.grails.datastore.gorm.GormEntity
import org.hibernate.SessionFactory
import org.hibernate.criterion.CriteriaSpecification
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.stereotype.Service

/**
 * 替换原来的GForm
 * <p>通过set到业务service，符合组合优于继承的设计规范，分离业务逻辑和数据库操作，为单元测试提供支持
 * <p>不涉及业务处理
 * Created by Neo on 2017-08-25.
 */
@Service
@Slf4j
@CompileStatic(TypeCheckingMode.SKIP)
class GormRepository implements GeneralRepository {
    @Autowired
    MessageSource messageSource
    @Autowired
    SessionFactory sessionFactory

    /**
     * @see GeneralRepository#get
     */
    @Override
    public <T> T get(Class<T> domain, def id) {
        domain.get(id)
    }

    /**
     * @see GeneralRepository#findFirst
     */
    @Override
    public <T> T findFirst(Class<T> domain, Map param) {
        param.maxResults = 1
        List results = list(domain, param)
        if (results)
            results.first()
        else
            null
    }

    /**
     * @see GeneralRepository#findByExample
     */
    @Override
    public <T> List<T> findByExample(T example) {
        example.class.findAll(example)
    }

    /**
     * 创建实体，使用HuTools接口
     *
     * org.apache.commons.beanutils.BeanUtils.populate功能较弱，已经试过，不支持“容错”、“默认值处理不正确”、“嵌套属性”
     * @see GeneralRepository#saveMap(Class, Map)
     */
    @Override
    public <T> T saveMap(Class<T> domain, Map map) {
        GormEntity newEntity = JsonUtil.mapToBean(map, domain)
        if (newEntity.ident()) {
            // 支持部分更新，所以map中只有部分属性，需要先从数据库中获取原实体
            GormEntity updateEntity = get(domain, newEntity.ident())
            /**
             * 支持部分属性更新,不能直接用bean做copy是因为bean会有初始话的值，要忽略map中没有的key值
             *
             * @see cn.hutool.core.bean.BeanUtil#fillBeanWithMap
             * hutool不支持trait属性，因为trait生成的field名带了前缀，而HuTools是根据field来copy的，不根据property
             */
            // 忽略map中没有或默认设置的的key
            def ignoreProps = BeanUtils.getPropertyDescriptors(domain)*.name.findAll {
                !map.containsKey(it) || domainUpdateIgnores.contains(it)
            }
            log.debug("ignoreProperties: {}", ignoreProps)
            BeanUtils.copyProperties(newEntity, updateEntity, ignoreProps.toArray(new String[0]))
            saveEntity(updateEntity)
        } else
            saveEntity(newEntity)
    }

    static final String[] domainUpdateIgnores = ['id', 'version', 'metaClass', 'lastUpdated', 'dateCreated']

    /**
     * 可能可以直接用attach，有案例的时候测试
     * @see org.grails.datastore.gorm.GormEntity#attach()
     * @see GeneralRepository#saveTransietEntity
     */
    Object saveTransietEntity(Object entity) {
        log.debug("saveTransietEntity $entity")
        GormEntity transietEntity = entity as GormEntity;
        GormEntity attachedEntity = GormEnhancer.findStaticApi(transietEntity.class).get(transietEntity.ident())
        if (attachedEntity) {//update
            if (attachedEntity.hasProperty('version'))
                log.debug("old version is $attachedEntity.version")

            BeanUtils.copyProperties(transietEntity, attachedEntity, domainUpdateIgnores)
            saveEntity(attachedEntity)
        } else //insert
            saveEntity(transietEntity)
    }

    /**
     * param中的关联属性查询默认都是inner join，
     * 如需outer join，不要用本方法，service中单独处理，指定JoinType.LEFT_OUTER_JOIN.joinTypeValue，
     * 前台参数传入也应该注意
     * @see GeneralRepository#list
     */
    @Override
    public <T> List<T> list(Class<T> domain, Map param) {
        log.info "list - ${domain} param:$param"

        if (param?.projections) {
            log.info "resultTransformer to CriteriaSpecification.ALIAS_TO_ENTITY_MAP"
            param.put 'resultTransformer', [
                    CriteriaSpecification.ALIAS_TO_ENTITY_MAP
            ]
        }

        criteriaQuery(domain, param, false)
    }

    /**
     * @see GeneralRepository#listAll
     */
    @Override
    public <T> List<T> listAll(Class<T> domain) {
        list(domain, null)
    }
    /**
     * @see GeneralRepository#count
     */
    @Override
    int count(Class domain, Map param) {
        log.info "count - ${domain} param:$param"
        criteriaQuery(domain, param, true)
    }

    /**
     * @see GeneralRepository#countAll
     */
    @Override
    int countAll(Class domain) {
        count(domain, null)
    }

    /**
     * @see GeneralRepository#delete
     */
    @Override
    void delete(Object entity) {
        entity.delete()
    }

    /**
     * @see GeneralRepository#deleteById
     */
    @Override
    Number deleteById(Class domain, Object id) {
        GormEnhancer.findStaticApi(domain).where {
            idEq id
        }.deleteAll()
    }

    /**
     * @see GeneralRepository#deleteByIds
     */
    @Override
    Number deleteByIds(Class domain, List idList) {
        GormEnhancer.findStaticApi(domain).where {
            inList('id', idList)
        }.deleteAll()
    }

    /**
     * @see GeneralRepository#deleteMatch
     */
    @Override
    Number deleteMatch(Class domain, Map param) {
        def criteria = GormCriteriaUtil.buildDetachedCriteria(domain, param)
        criteria ? criteria.deleteAll() : 0
    }

    /**
     * @see GeneralRepository#updateMatch
     */
    @Override
    Number updateMatch(Class domain, Map param, Map properties) {
        def criteria = GormCriteriaUtil.buildDetachedCriteria(domain, param)
        criteria ? criteria.updateAll(properties) : 0
    }

    /**
     * @see GeneralRepository#saveEntity(Object)
     * @see org.springframework.validation.Errors
     */
    @Override
    def saveEntity(Object entity) {
        log.info "saveEntity  ${entity.class}"

        GormEntity gormEntity = entity as GormEntity;
        //错误信息可能在中间过程中产生，末状态正确时不一定为空，所以必须先清理
        gormEntity.clearErrors()
        if (gormEntity.validate()) {
            /* 如果存在unique限制，当save为update时导致NonUniqueObjectException异常
             * 原因是查询是否唯一时，导致session有两个id相同对象，grails目前处理不够好。
             * 如果用entity.merge()，部分功能，如autoTimeStrape无法自动完成
             * 也可通过清空session处理
             * 20170929 新版gorm中没有发现这个问题，isMerge暂不使用
             *
			 * if(isMerge){
			 * 	entity.merge()
		     * }
             * */
            log.debug "saveEntity ${gormEntity}"
            gormEntity.save()
        } else {
            StringBuilder sb = new StringBuilder()
            gormEntity.errors.allErrors.each {
                String msg = messageSource.getMessage(it, Locale.default);
                log.error(msg)
                log.error("error.code配置参考：{}", it.toString())
                sb.append("$msg\n")

            }
            throw new MatrixException('SaveEntityFail', sb)
        }
    }

    /**
     * @see GeneralRepository#flush
     */
    def flush() {
        sessionFactory.currentSession.flush()
    }
    /**
     * @see GeneralRepository#clear
     */
    def clear() {
        sessionFactory.currentSession.clear()
    }

    /**
     * @see GeneralRepository#executeUpdate
     */
    Integer executeUpdate(String ql, Map<String, Object> param) {
        def query = sessionFactory.currentSession.createQuery(ql)
        param.each { query.setParameter(it.key, it.value) }
        query.executeUpdate()
    }
    /**
     * 使用hibernate criteria 查询
     * <p>本方法为hibernate专用，如需使用需继承本类
     * @param domainClass
     * @param param
     * @param isCount 是否计数
     * @return
     * @see grails.orm.HibernateCriteriaBuilder
     */
    protected Object criteriaQuery(Class domainClass, Map param, boolean isCount) {
        def criteria = domainClass.createCriteria()
        if (isCount)
            criteria.count(GormCriteriaUtil.makeCriteria(param))
        else
            criteria.list(GormCriteriaUtil.makeCriteria(param))
    }

}
