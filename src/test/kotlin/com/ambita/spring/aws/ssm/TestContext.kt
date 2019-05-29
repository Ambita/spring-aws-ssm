package com.ambita.spring.aws.ssm

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@Configuration
@PropertySource(
        name = "SSM yaml file",
        factory = YamlPropertySourceFactory::class,
        value = ["ssm://eu-central-1/components/spring-aws-ssm/itest/springboot.yaml"]
)

class TestContext {

    @Bean
    fun makeParamStoreContextInitializer(): ParamStoreContextInitializer {
        return ParamStoreContextInitializer()
    }
}
