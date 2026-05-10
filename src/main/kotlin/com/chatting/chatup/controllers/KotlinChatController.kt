package com.chatting.chatup.controllers

import com.chatting.chatup.config.MemoryService
import com.chatting.chatup.config.WebService
import com.chatting.chatup.dtos.MessagePromt
import com.chatting.chatup.enums.Memory
import com.chatting.chatup.enums.Roles
import jakarta.servlet.http.HttpSession
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader

@Controller
class KotlinChatController(
    private val webService: WebService,
    private val memoryService: MemoryService
) {
    private val LOG = LoggerFactory.getLogger(KotlinChatController::class.java)


    @GetMapping("/")
    fun index(
        model: Model,
        session: HttpSession,
    ): String {
        model.addAttribute("chatHistory", memoryService.getHistory(session.id))
        model.addAttribute("messagePromt", MessagePromt())
        model.addAttribute("roles", Roles.values())
        model.addAttribute("memorySelect", Memory.values())
        return "chatroom"
    }


    @PostMapping("/api/v1/chat")
    fun postMessage(
        @ModelAttribute("messagePromt") @Valid message: MessagePromt,
        result: BindingResult,
        model: Model,
        session: HttpSession,
        @RequestHeader(value = "HX-Request", required = false) htmxRequest: String?
    ): String {
        model.addAttribute("roles", Roles.values())
        model.addAttribute("memorySelect", Memory.values())
        if (result.hasErrors()) {
            LOG.error("input promt has errors: {}", result.fieldError)
            return "chatroom :: errorBlock"
        }

        webService.askAi( message, session.id)

        model.addAttribute("chatHistory", memoryService.getHistory(session.id))
        if (htmxRequest != null) {
            return "chatroom :: messageList"
        }
        return "chatroom"
    }


}
