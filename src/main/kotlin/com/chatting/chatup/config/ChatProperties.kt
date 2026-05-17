package com.chatting.chatup.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "chat.api")
class ChatProperties {
    lateinit var apiKey: String
    lateinit var baseUrl: String
    lateinit var modelName: String


}
