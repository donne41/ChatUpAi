package com.chatting.chatup.controllers

import org.springframework.ai.ollama.OllamaChatModel
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class KotlinChatController(val aiModel: OllamaChatModel) {

    private val chatModel: OllamaChatModel = aiModel;



    @GetMapping("/")
    fun index(): ResponseEntity<MessageResponse> {
        return ResponseEntity(MessageResponse("HELLO", "WORLD"), HttpStatus.OK);
    }

    @GetMapping("/ai/chat")
    fun chat(@RequestParam(value = "message") message: String): String? {
        return chatModel.call(message);
    }
}
