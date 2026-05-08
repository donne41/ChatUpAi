package com.chatting.chatup.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.client.RestClient

@Configuration
class RestClientConfig {
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

}
