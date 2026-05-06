package com.chatting.chatup.config


import com.chatting.chatup.dtos.*
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

    private val memoryMap: HashMap<String, MutableList<ResponseMessage>> = HashMap()

    @Bean
    fun webClient(): RestClient {
        return RestClient.builder()
            .baseUrl(postUrl)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer $apiKey")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
            .build()
    }

    fun askAi(userPromt: MessagePromt, sessionId: String): String? {
        val dataRequest = DataRequest(
            model = modelName,
            messages = listOf(
                Message(
                    role = "system",
                    content = userPromt.role.promt
                ),
                Message(
                    role = "user",
                    content = userPromt.promt
                )
            )
        )
        val history = memoryMap.computeIfAbsent(sessionId) { mutableListOf() }
        history.add(ResponseMessage("user", userPromt.promt))


        val dataResponse = webClient().post()
            .contentType(MediaType.APPLICATION_JSON)
            .body(dataRequest)
            .retrieve()
            .body(DataResponse::class.java)
        //.body(String::class.java)
        history.add(ResponseMessage("assistant", dataResponse?.choices?.firstOrNull()?.message?.content.toString()))

        //return dataResponse
        return history.last().content //probably not right. check later, push to laptop to get som sunK
        //return dataResponse?.choices?.firstOrNull()?.message?.content ?: "Hoppsan, Fick inget svar!"
    }
}
