package com.ambita.spring.aws.ssm

import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.io.ProtocolResolver
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader

/**
 * This class has to be added to Tests and SpringApplications that uses *AWS
 * Parameter Store*.
 *
 * **application.yaml overrides the values from the @PropertySource!!!**
 */
class ParamStoreContextConfiguration : ApplicationContextInitializer<ConfigurableApplicationContext> {

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        LOG.info("Adding " + ParamStoreResourceLoader::class.java + " to ResourceResolver")

        val ssmProtocolResolver = object : ProtocolResolver {
            val paramStoreResourceLoader = ParamStoreResourceLoader(applicationContext.environment)

            override fun resolve(location: String, resourceLoader: ResourceLoader): Resource? {
                return if (location.startsWith("ssm://")) {
                    paramStoreResourceLoader.getResource(location)
                } else {
                    null
                }
            }
        }

        applicationContext.addProtocolResolver(ssmProtocolResolver)
    }

    companion object {

        private val LOG = LoggerFactory.getLogger(ParamStoreContextConfiguration::class.java)
    }
}
