package com.chatting.chatup.controllers

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

class chatClient {

    val customClient = RestClient.builder()
        .requestFactory(HttpComponentsClientHttpRequestFactory())
        .baseUrl("https://openrouter.ai/api/v1/chat/completions")
        .build();
}