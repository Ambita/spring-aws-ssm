package com.ambita.spring.aws.ssm

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.boot.SpringApplication

class ParamStoreApplicationTest {

    @AfterEach
    fun afterEach() {
        System.clearProperty("spring.profiles.active")
    }

    @Test
    fun testRuntimeProfile() {
        System.setProperty("spring.profiles.active", "itest")
        main(arrayOf())
    }

    companion object {

        internal fun main(args: Array<String>) {

            val appl = SpringApplication(TestContext::class.java)
            appl.addInitializers(ParamStoreContextConfiguration())
            appl.run(*args)
        }
    }

    /*  @Test
  void testRuntimeProfile_default() {
    main(new String[0]);
  }*/
}
