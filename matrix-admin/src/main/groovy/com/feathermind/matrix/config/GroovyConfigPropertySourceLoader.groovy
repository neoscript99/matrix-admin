package com.feathermind.matrix.config


import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.boot.env.PropertySourceLoader
import org.springframework.core.env.MapPropertySource
import org.springframework.core.env.PropertySource
import org.springframework.core.io.Resource


/**
 * 加载 'application.groovy'
 *
 */
@CompileStatic
@Slf4j
class GroovyConfigPropertySourceLoader implements PropertySourceLoader {

    final String[] fileExtensions = ['groovy'] as String[]


    @Override
    List<PropertySource> load(String name, Resource resource) throws IOException {

        if (!resource.exists())
            return Collections.emptyList()

        ConfigSlurper configSlurper = new ConfigSlurper()
        //可以传入参数，在application.groovy中的${}获取
        configSlurper.setBinding(createBinding());
        def configObject = configSlurper.parse(resource.URL)
        return Collections.<PropertySource> singletonList(new MapPropertySource(name, configObject.flatten()));
    }

    private Map<String, Object> createBinding() {
        final Map<String, Object> bindings = new HashMap<>();
        bindings.put("userHome", System.getProperty("user.home"));
        bindings.put("appDir", System.getProperty("user.dir"));
        //bindings.put("springProfile", profile);
        return bindings;
    }

}
