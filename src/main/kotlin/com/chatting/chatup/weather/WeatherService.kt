package com.chatting.chatup.weather

import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class WeatherService(builder: WebClient.Builder) {
    private val webClient = builder.baseUrl("https://api.open.meteo.com/v1").build();
    private val gotherburglat: String = "57.7072";
    private val gotherburglong: String = "11.9668";
    private val apiString: String =
        "/forecast?latitude=57.7072&longitude=11.9668&hourly=temperature_2m&timezone=Europe%2FBerlin"

    fun getGothenburgWeather(): String? {
        return webClient.get()
            .uri(apiString)
            .retrieve()
            .bodyToMono(String::class.java)
            .block()
    }
}
