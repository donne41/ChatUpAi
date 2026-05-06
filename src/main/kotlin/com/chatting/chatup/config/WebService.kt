package com.chatting.chatup.config


import com.chatting.chatup.config.chatService.*
import com.chatting.chatup.dtos.MessagePromt
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.client.RestClient

@Configuration
class WebService(private val chatService: chatService) {

    @Value("\${API_KEY}")
    private lateinit var apiKey: String
    private val postUrl = "https://openrouter.ai/api/v1/chat/completions"

    @Bean
    fun webClient(): RestClient {
        return RestClient.builder()
            .baseUrl(postUrl)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer $apiKey")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
            .build()
    }

    fun askAi(userPromt: MessagePromt): String {
        val dataRequest = DataRequest(
            model = "nvidia/nemotron-3-nano-omni-30b-a3b-reasoning:free",
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


        val dataResponse = webClient().post()
            .contentType(MediaType.APPLICATION_JSON)
            .body(dataRequest)
            .retrieve()
            .body(DataResponse::class.java)

        return dataResponse?.choices?.firstOrNull()?.message?.content ?: "Hoppsan, Fick inget svar!"
    }
}
