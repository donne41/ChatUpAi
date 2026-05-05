package com.chatting.chatup.controllers

import com.chatting.chatup.config.WebService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping

@Controller
class KotlinChatController(
    webService: WebService
) {

    private val webService = webService



    @GetMapping("/")
    fun index(@ModelAttribute("chatMessage") message: String): String {
        return "chatroom"
    }


    @PostMapping("/chat")
    fun postMessage(
        @ModelAttribute("chatMessage") message: String,
        model: Model
    ): String {
        val response = webService.askAi(message)
        //println(response)
        model.addAttribute("responseMessage", response)
        return "chatroom"
    }

}
