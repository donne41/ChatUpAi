package com.chatting.chatup.config

import org.springframework.stereotype.Service

@Service
class chatService {


    data class DataRequest(
        val model: String,
        val messages: List<Message>
    )

    data class Message(
        val role: String,
        val content: String
    )

    data class DataResponse(
        val choices: List<Choice>
    )

    data class Choice(
        val message: ResponseMessage
    )

    data class ResponseMessage(
        val role: String,
        val content: String
    )
}
