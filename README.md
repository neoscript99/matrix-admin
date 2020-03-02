## 通知内容参考

[关于做好2019年鄞州区教育科学规划课题申报工作的通知](http://www.nbyzedu.cn/jyky/jyke_tzgg/201901/t20190116_564413.html)

## 待开发内容

### 立项流程
- [x] 课题负责人维护
- [ ] 上传申报盲评文本


## 问题

### matrix-admin中初始化的user、department不能在科研系统中做关联，否则会报错，因为科研系统依赖的是res_user和res_dept。
```
org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException: Referential integrity constraint violation: "FKI8HTF8427NPYV9UDS7WDLW077: PUBLIC.RES_TOPIC FOREIGN KEY(DEPT_ID) REFERENCES PUBLIC.RES_RES_DEPT(ID) ('df56d52d-a5f4-4bea-8778-ff4ef9021127')"; SQL statement:
insert into res_topic (finish_day, topic_status_code, research_content_code, date_created, dept_id, last_updated, topic_cert, person_in_charge_id, topic_name, topic_source_code, research_subject_code, main_report_id, prepare_achieve_form_code, topic_code, initial_report_id, prepare_finish_day, topic_cate_code, achieve_form_code, research_target_code, id) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) [23506-200]
	at org.h2.message.DbException.getJdbcSQLException(DbException.java:459) ~[h2-1.4.200.jar:1.4.200]
	at org.h2.message.DbException.getJdbcSQLException(DbException.java:429) ~[h2-1.4.200.jar:1.4.200]
	......
	at com.feathermind.matrix.controller.DomainController.save(DomainController.groovy:58) ~[main/:na]
```

### 前端构建失败
详情：
```
FATAL ERROR: Ineffective mark-compacts near heap limit Allocation failed - JavaScript heap out of memory
```

原因：依赖`xlsx`导出包后，`yarn buld`内存溢出

解决方案：设置环境变量 NODE_OPTIONS=--max_old_space_size=8192
