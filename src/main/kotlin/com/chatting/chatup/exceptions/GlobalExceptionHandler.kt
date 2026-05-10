package com.chatting.chatup.exceptions

import com.chatting.chatup.dtos.MessagePromt
import org.springframework.http.HttpStatus
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ClientSideException::class)
    fun handleClientError(error: ClientSideException, model: Model): String {
        model.addAttribute("errorMessage", error.message)
        model.addAttribute("messagePromt", MessagePromt())
        return "chatroom :: errorBlock"
    }

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(ApiServiceException::class)
    fun handleServiceError(error: ApiServiceException, model: Model): String {
        model.addAttribute("errorMessage", error.message)
        return "chatroom :: errorBlock"
    }
}
