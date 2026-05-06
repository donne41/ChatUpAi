package com.chatting.chatup.dtos

data class DataRequest(
    val model: String,
    val messages: List<Message>
)
