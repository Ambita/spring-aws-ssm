package com.ambita.spring.aws.ssm


import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension


@SpringBootTest(classes = [TestContext::class])
@ExtendWith(SpringExtension::class)
@ContextConfiguration(initializers = [ParamStoreContextConfiguration::class])
@ActiveProfiles("itest")
class ParamStorePropertySourceIT @Autowired
constructor(
        @param:Value("\${myconfig.from-ssm}") private val fromSsm: String,
        @param:Value("\${myconfig.from-yaml}") private val fromYaml: String
) {

    @Test
    fun testMe() {
        assertEquals("This comes from SSM as it should", fromSsm, "fromSsm")
        assertEquals("From the application.yaml file", fromYaml, "fromYaml")
    }
}