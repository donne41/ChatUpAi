package com.chatting.chatup.config

import com.chatting.chatup.dtos.Message
import org.springframework.stereotype.Service

@Service
class MemoryService {
    private val memoryMap: HashMap<String, MutableList<Message>> = HashMap()

    fun getHistory(sessionId: String): List<Message> {
        return memoryMap.getOrDefault(sessionId, mutableListOf())
    }

    fun addHistory(sessionId: String, message: Message) {
        var history = memoryMap.computeIfAbsent(sessionId) { mutableListOf() }
        history.add(message)

        if (history.size > 20) {
            history.removeFirst()
        }
    }
}
