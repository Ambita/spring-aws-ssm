package com.ambita.spring.aws.ssm

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.stereotype.Component

@Component
class ParamStoreContextInitializer : InitializingBean {
    @Autowired
    private val configurableEnvironment: ConfigurableEnvironment? = null

    override fun afterPropertiesSet() {
        val propertySources = configurableEnvironment!!.propertySources

        val ssmPropertySources = propertySources
                .filter { it.name.startsWith("SSM") }
                .map { it.name to it!! }
                .toMap()

        val testSource = propertySources.get("test")
        if (testSource != null) {
            LOG.info("Has test source \"{}\"", TEST_PROPERTIES_FILE_NAME)
        } else {
            LOG.debug("No test-source found. \"{}\"", TEST_PROPERTIES_FILE_NAME)
        }

        ssmPropertySources.keys.forEach { propertySources.remove(it) }

        val sortedKeysBackwards = ssmPropertySources.keys
                .toList()
                .sorted()
                .reversed()

        LOG.debug(
                "Removeing and adding properties sources first: {}",
                sortedKeysBackwards.toTypedArray()
        )

        sortedKeysBackwards.forEach { name ->
            val propertySource = (ssmPropertySources[name]
                    ?: error("property with name $name is not present"))
            propertySources.addFirst(propertySource)
        }

        if (testSource != null) {
            propertySources.addFirst(testSource)
        }

        propertySources.stream().forEach { LOG.info("PropertySource: {}", it.name) }
    }

    companion object {

        private val LOG = LoggerFactory.getLogger(ParamStoreContextInitializer::class.java)
        private val TEST_PROPERTIES_FILE_NAME = "file://\${HOME}/.spring-boot-devtools.properties"
    }
}


