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
    private val webClient: RestClient
) {

    private val log = LoggerFactory.getLogger(this::class.java)
    private val modelName = "nvidia/nemotron-3-nano-omni-30b-a3b-reasoning:free"


    fun askAi(userPromt: MessagePromt, sessionId: String) {
        val previousMessages = mutableListOf<Message>()
        previousMessages.add(
            Message(
                role = "system", userPromt.role.promt
            )
        )

        if (userPromt.memory.value > 0) {
            val fullHistory = memoryService.getHistory(sessionId)
            val messageToFetch = minOf(fullHistory.size, userPromt.memory.value)
            if (messageToFetch > 0) {
                val historySlice = fullHistory.subList(fullHistory.size - messageToFetch, fullHistory.size)
                previousMessages.addAll(historySlice)
            }
        }
        previousMessages.add(Message("user", userPromt.promt))

        val dataRequest = DataRequest(
            model = modelName,
            messages = previousMessages
        )

        memoryService.addHistory(sessionId, Message("user", userPromt.promt))

        val dataResponse = webClient.post()
            .contentType(MediaType.APPLICATION_JSON)
            .body(dataRequest)
            .retrieve()
            .onStatus({ status -> status.is4xxClientError }) { request, response ->
                throw ClientSideException(response.statusText)
            }
            .onStatus({ status -> status.is5xxServerError }) { request, response ->
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
