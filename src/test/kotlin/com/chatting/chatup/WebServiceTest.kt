package com.chatting.chatup

import com.chatting.chatup.config.MemoryService
import com.chatting.chatup.config.WebService
import com.chatting.chatup.dtos.Message
import com.chatting.chatup.dtos.MessagePromt
import com.chatting.chatup.enums.Memory
import com.chatting.chatup.enums.Roles
import com.github.tomakehurst.wiremock.client.WireMock.*
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.wiremock.spring.EnableWireMock


@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = [
        "chat.api.base-url=http://localhost:\${wiremock.server.port}",
        "chat.api.api-key=testkey",
    ]
)
@EnableWireMock
class WebServiceTest(
    @Autowired val service: WebService,
    @Autowired val memory: MemoryService

) {

    @Test
    fun postMessageWithNoMemoryShouldOnlyHaveLengthOfTwo() {
        val sessionId = "session.id"
        val fakeMessage = MessagePromt(
            role = Roles.ASSISTANT,
            memory = Memory.NONE,
            promt = "test"
        )

        stubFor(
            post(urlEqualTo("/"))
                .willReturn(
                    aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                            """
                        {
                        "choices": [
                        {
                        "message": {
                        "role": "assistant",
                        "content": "test response"
                        }}]}
                    """.trimIndent()
                        )
                )
        )

        service.askAi(fakeMessage, sessionId)
        val result = memory.getHistory(sessionId)

        assertThat(result.last().role).isEqualTo("assistant")
        assertThat(result.last().content).isEqualTo("test response")

        assertThat(result.getOrNull(0)?.role).isEqualTo("user")
        assertThat(result.getOrNull(0)?.content).isEqualTo("test")
        assertThat(result.size).isEqualTo(2)

    }

    @Test
    fun postWithMemoryShouldBeWithCorrectMemory() {
        val sessionId = "session.id2"
        memory.addHistory(sessionId, Message(role = "user", "one"))
        memory.addHistory(sessionId, Message(role = "user", "two"))
        memory.addHistory(sessionId, Message(role = "user", "three"))
        memory.addHistory(sessionId, Message(role = "user", "four"))
        memory.addHistory(sessionId, Message(role = "user", "five"))
        memory.addHistory(sessionId, Message(role = "user", "six"))
        memory.addHistory(sessionId, Message(role = "user", "seven"))
        memory.addHistory(sessionId, Message(role = "user", "eight"))
        memory.addHistory(sessionId, Message(role = "user", "nine"))
        memory.addHistory(sessionId, Message(role = "user", "ten"))

        val fakeMessage = MessagePromt(
            Roles.ASSISTANT,
            Memory.LOW,
            "test"
        )

        stubFor(
            post(urlEqualTo("/"))
                .willReturn(
                    aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                            """
                        {
                        "choices": [
                        {
                        "message": {
                        "role": "assistant",
                        "content": "test response"
                        }}]}
                    """.trimIndent()
                        )
                )
        )

        service.askAi(fakeMessage, sessionId)

        val result = memory.getHistory(sessionId)
        assertThat(result.size).isEqualTo(12)
    }
}
