package com.chatting.chatup.config


import com.chatting.chatup.dtos.DataRequest
import com.chatting.chatup.dtos.DataResponse
import com.chatting.chatup.dtos.Message
import com.chatting.chatup.dtos.MessagePromt
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.client.RestClient

@Configuration
class WebService(private val chatService: chatService) {

    private val log = LoggerFactory.getLogger(this::class.java)

    @Value("\${API_KEY}")
    private lateinit var apiKey: String
    private val postUrl = "https://openrouter.ai/api/v1/chat/completions"
    private val modelName = "nvidia/nemotron-3-nano-omni-30b-a3b-reasoning:free"

    private val memoryMap: HashMap<String, MutableList<Message>> = HashMap()

    @Bean
    fun webClient(): RestClient {
        return RestClient.builder()
            .baseUrl(postUrl)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer $apiKey")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
            .build()
    }
    fun getHistory(sessionId: String): List<Message> {
        return memoryMap.getOrDefault(sessionId, mutableListOf())
    }

    fun addHistory(sessionId: String, message: Message) {
        var history = memoryMap.computeIfAbsent(sessionId) { mutableListOf() }
        history.add(message)

        if (history.size > 20){
            history.removeFirst()
        }
    }


    fun askAi(userPromt: MessagePromt, sessionId: String) {
        val previousMessages = mutableListOf<Message>()
        previousMessages.add(
            Message(
                role = "system", userPromt.role.promt
            )
        )

        if (userPromt.memory.value > 0) {
            val fullHistory = getHistory(sessionId)
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

        addHistory(sessionId, Message("user", userPromt.promt))
        log.info("added message to history, Current size of messages sending: " + previousMessages.size)

        val dataResponse = webClient().post()
            .contentType(MediaType.APPLICATION_JSON)
            .body(dataRequest)
            .retrieve()
            .body(DataResponse::class.java)
        addHistory(sessionId, Message("assistant", dataResponse?.choices?.firstOrNull()?.message?.content.toString()))
    }
}
