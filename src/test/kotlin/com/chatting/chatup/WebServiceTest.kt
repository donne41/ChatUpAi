package com.chatting.chatup

import com.chatting.chatup.config.WebService
import com.github.tomakehurst.wiremock.client.WireMock.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.wiremock.spring.EnableWireMock


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableWireMock
class WebServiceTest(@Autowired val service: WebService) {

    @Test
    fun testGet() {
        stubFor(
            get(urlEqualTo("/"))
                .willReturn(aResponse().withStatus(201).withBody("{}"))
        )

        //service.askAi(userPromt = MessagePromt("user", "random"))
    }
}
