package com.chatting.chatup.dtos

import com.chatting.chatup.config.Memory
import com.chatting.chatup.config.Roles

data class MessagePromt(val role: Roles, val memory: Memory, val promt: String) {
    constructor() : this(Roles.ASSISTANT, Memory.LOW, "")
}
