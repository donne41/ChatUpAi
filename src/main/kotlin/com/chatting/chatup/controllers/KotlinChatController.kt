package com.chatting.chatup.controllers

import com.chatting.chatup.config.WebService
import com.chatting.chatup.weather.WeatherService
import org.springframework.ai.ollama.OllamaChatModel
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping

@Controller
class KotlinChatController(
    webService: WebService,
    aiModel: OllamaChatModel,
    weatherService: WeatherService) {

    private val webService = webService



    @GetMapping("/")
    fun index(): String = "chatroom"


    @PostMapping("/chat")
    fun postMessage(@ModelAttribute("chatMessage") message: String): String {
        val response = webService.askAi(message)
        println(response)
        return "chatroom"
    }
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
