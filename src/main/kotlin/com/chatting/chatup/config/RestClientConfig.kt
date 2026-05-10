package com.chatting.chatup.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.client.RestClient

@Configuration
class RestClientConfig(private val chatProperties: ChatProperties) {



    @Bean
    fun webClient(): RestClient {
        return RestClient.builder()
            .baseUrl(chatProperties.baseUrl)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + chatProperties.apiKey)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
            .build()
    }

}
