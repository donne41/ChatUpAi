package com.chatting.chatup.service

import com.chatting.chatup.dtos.Message
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class MemoryService {
    private val memoryMap: ConcurrentHashMap<String, MutableList<Message>> = ConcurrentHashMap()

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
