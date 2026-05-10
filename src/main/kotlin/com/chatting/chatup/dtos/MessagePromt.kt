package com.chatting.chatup.dtos

import com.chatting.chatup.enums.Memory
import com.chatting.chatup.enums.Roles
import jakarta.validation.constraints.NotBlank

data class MessagePromt(
    val role: Roles,
    val memory: Memory,
    @field:NotBlank(message = "Empty promt is not allowed.")
    val promt: String
) {
    constructor() : this(Roles.ASSISTANT, Memory.LOW, "")
}
