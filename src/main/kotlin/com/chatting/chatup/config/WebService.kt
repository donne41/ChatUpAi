package com.chatting.chatup.config


import com.chatting.chatup.dtos.DataRequest
import com.chatting.chatup.dtos.DataResponse
import com.chatting.chatup.dtos.Message
import com.chatting.chatup.dtos.MessagePromt
import com.chatting.chatup.exceptions.ApiServiceException
import com.chatting.chatup.exceptions.ClientSideException
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

@Service
class WebService(
    private val memoryService: MemoryService,
    private val webClient: RestClient,
    private val chatProperties: ChatProperties
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    fun buildDataRequest(userPromt: MessagePromt, history: List<Message>): DataRequest {
        val previousMessages = mutableListOf<Message>()
        previousMessages.add(
            Message(
                role = "system", userPromt.role.promt
            )
        )

        if (userPromt.memory.value > 0) {
            val messageToFetch = minOf(history.size, userPromt.memory.value)
            if (messageToFetch > 0) {
                val historySlice = history.subList(history.size - messageToFetch, history.size)
                previousMessages.addAll(historySlice)
            }
        }
        previousMessages.add(Message("user", userPromt.promt))

        return DataRequest(
            model = chatProperties.modelName,
            messages = previousMessages
        )
    }

    fun askAi(userPromt: MessagePromt, sessionId: String) {

        val history = memoryService.getHistory(sessionId)
        val dataRequest = buildDataRequest(userPromt, history)

        memoryService.addHistory(sessionId, Message("user", userPromt.promt))

        val dataResponse = webClient.post()
            .contentType(MediaType.APPLICATION_JSON)
            .body(dataRequest)
            .retrieve()
            .onStatus({ status -> status.is4xxClientError }) { request, response ->
                val errorBody = response.body.bufferedReader().use { it.readText() }
                log.error("Error from client, answer: {}", errorBody)
                throw ClientSideException(response.statusText)
            }
            .onStatus({ status -> status.is5xxServerError }) { request, response ->
                val errorBody = response.body.bufferedReader().use { it.readText() }
                log.error("Error from server, answer: {}", errorBody)
                throw ApiServiceException("Ai server error: " + response.statusText)
            }
            .body(DataResponse::class.java)
        memoryService.addHistory(
            sessionId,
            Message("assistant",
                dataResponse?.choices?.firstOrNull()?.message?.content
                    ?: "Sorry, i could not generate a answer on that."
            )
        )

    }
}
