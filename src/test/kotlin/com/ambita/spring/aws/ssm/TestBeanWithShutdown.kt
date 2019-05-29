package com.ambita.spring.aws.ssm

import org.slf4j.LoggerFactory
import org.springframework.beans.BeansException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
@Lazy(false)
class TestBeanWithShutdown @Autowired
constructor(
        @param:Value("\${hest.miljoe}") private val miljoe: String) : ApplicationContextAware {

    private var applicationContext: ApplicationContext? = null

    @PostConstruct
    fun postConstruct() {
        LOG.info("Shutdown myself! Found miljoe: $miljoe")
        (applicationContext as ConfigurableApplicationContext).close()
    }

    @Throws(BeansException::class)
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.applicationContext = applicationContext
    }

    companion object {

        private val LOG = LoggerFactory.getLogger(TestBeanWithShutdown::class.java)
    }
}
