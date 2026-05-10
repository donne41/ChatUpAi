package com.chatting.chatup.exceptions

import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ClientSideException::class)
    fun handleClientError(error: ClientSideException, model: Model): String {
        model.addAttribute("errorMessage", error.message)
        return "chatroom :: exceptionBlock"
    }

    @ExceptionHandler(ApiServiceException::class)
    fun handleServiceError(error: ApiServiceException, model: Model): String {
        model.addAttribute("errorMessage", error.message)
        return "chatroom :: exceptionBlock"
    }
}
