package com.chatting.chatup.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class KotlinChatController {

    @GetMapping("/")
    fun index(): ResponseEntity<MessageResponse> {
        return ResponseEntity(MessageResponse("HELLO", "WORLD"), HttpStatus.OK);
    }
}
