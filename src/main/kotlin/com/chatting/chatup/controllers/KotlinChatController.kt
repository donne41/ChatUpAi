package com.chatting.chatup.controllers

import com.chatting.chatup.weather.WeatherService
import org.springframework.ai.ollama.OllamaChatModel
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Controller
class KotlinChatController(
    aiModel: OllamaChatModel,
    weatherService: WeatherService) {



    @GetMapping("/")
    fun index(): String = "chatroom"

//    @GetMapping("/ai/chat")
//    fun chat(@RequestParam(value = "message") message: String): String? {
//        return chatModel.call(message);
//    }
//
//    @GetMapping("/ai/weather")
//    fun getGotherburgWeater(): String? {
//        val rawWeatherData = weatherService.getGothenburgWeather() ?: return "Kunde inte hämta vädret just nu";
//
//        val promt = """
//            Här är rådata om vädret i Göteborg just nu: $rawWeatherData
//            skriv en kort och trevlig sammanfattning på svenska baserat på datan
//        """.trimIndent()
//
//        return chatModel.call(promt);
//    }
}
