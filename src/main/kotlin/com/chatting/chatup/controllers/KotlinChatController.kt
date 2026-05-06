package com.chatting.chatup.controllers

import com.chatting.chatup.config.Memory
import com.chatting.chatup.config.Roles
import com.chatting.chatup.config.WebService
import com.chatting.chatup.config.chatService
import com.chatting.chatup.dtos.MessagePromt
import jakarta.servlet.http.HttpSession
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
    fun index(
        model: Model,
        session: HttpSession
    ): String {
        model.addAttribute("chatHistory", webService.getHistory(session.id))
        model.addAttribute("messagePromt", MessagePromt())
        model.addAttribute("roles", Roles.values())
        model.addAttribute("memorySelect", Memory.values())
        return "chatroom"
    }


    @PostMapping("/chat")
    fun postMessage(
        @ModelAttribute("messagePromt") message: MessagePromt,
        model: Model,
        session: HttpSession,
    ): String {
        model.addAttribute("roles", Roles.values())
        model.addAttribute("memorySelect", Memory.values())
        webService.askAi( message, session.id)
        //println(response)
        model.addAttribute("chatHistory", webService.getHistory(session.id))
        return "chatroom"
    }


}
