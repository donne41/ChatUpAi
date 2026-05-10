package com.chatting.chatup

import com.chatting.chatup.config.MemoryService
import com.chatting.chatup.config.WebService
import com.chatting.chatup.dtos.Message
import com.chatting.chatup.dtos.MessagePromt
import com.chatting.chatup.enums.Memory
import com.chatting.chatup.enums.Roles
import com.chatting.chatup.exceptions.ApiServiceException
import com.chatting.chatup.exceptions.ClientSideException
import com.github.tomakehurst.wiremock.client.WireMock.*
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.wiremock.spring.EnableWireMock
import java.util.*


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
    @Autowired val memory: MemoryService,

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

        val fakeMessage = MessagePromt(
            Roles.ASSISTANT,
            Memory.LOW,
            "test"
        )
        memory.addHistory(sessionId, Message("user", "test"))

        val result = service.buildDataRequest(fakeMessage, memory.getHistory(sessionId))

        assertThat(result.messages.first().role).isEqualTo("system")
        assertThat(result.messages.first().content).isEqualTo(Roles.ASSISTANT.promt)

        assertThat(result.messages.size).isEqualTo(3)
    }

    @Test
    fun postCheckWithMemoryBetweenCalls() {
        val lastSessionId = "session.id2"
        val sessionId = "session.id3"
        val fakeMessage = MessagePromt(
            Roles.ASSISTANT,
            Memory.LOW,
            "test"
        )

        val result1 = service.buildDataRequest(fakeMessage, memory.getHistory(sessionId))
        val result2 = service.buildDataRequest(fakeMessage, memory.getHistory(lastSessionId))
        assertThat(result1.messages.size).isEqualTo(2)
        assertThat(result2.messages.size).isEqualTo(3)

    }

    @Test
    fun postWith400ErrorthrowsException() {
        val sessionid = UUID.randomUUID().toString()

        val fakeMessage = MessagePromt(
            Roles.ASSISTANT,
            Memory.LOW,
            "test"
        )

        stubFor(
            post(urlEqualTo("/"))
                .willReturn(
                    status(401)
                        .withStatusMessage("Unauthorized")
                )
        )

        val ex = assertThrows<ClientSideException> { service.askAi(fakeMessage, sessionid) }

        assertThat(ex.message).isEqualTo("Unauthorized")

    }

    @Test
    fun postWith500ErrorThrowsException() {
        val sessionid = UUID.randomUUID().toString()

        val fakeMessage = MessagePromt(
            Roles.ASSISTANT,
            Memory.LOW,
            "test"
        )

        stubFor(
            post(urlEqualTo("/"))
                .willReturn(status(500))
        )

        val ex = assertThrows<ApiServiceException> { service.askAi(fakeMessage, sessionid) }

        assertThat(ex.message).isEqualTo("Ai server error: Server Error")
    }
}
