package com.ambita.spring.aws.ssm

import org.slf4j.LoggerFactory
import org.springframework.boot.env.YamlPropertySourceLoader
import org.springframework.core.env.PropertySource
import org.springframework.core.io.support.DefaultPropertySourceFactory
import org.springframework.core.io.support.EncodedResource
import java.io.IOException

class YamlPropertySourceFactory : DefaultPropertySourceFactory() {

    override fun createPropertySource(name: String?, resource: EncodedResource): PropertySource<*> {
        LOG.debug("Loading yaml for ", resource)
        try {
            val pss = YamlPropertySourceLoader()
                    .load(
                            name,
                            resource.resource
                    )

            if (pss.size != 1) {
                val msg = "Did not expect more than one PropertySource from YamlProperySourceLoader. got " + pss.size
                LOG.error(msg)
                throw IllegalStateException(msg)
            }
            return pss[0]
        } catch (e: IOException) {
            throw RuntimeException("Not implemented!")
        }

    }

    companion object {

        private val LOG = LoggerFactory.getLogger(YamlPropertySourceFactory::class.java)
    }
}
