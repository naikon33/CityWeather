package com.example.cityweather.model

data class CityWithWeather(
    val id: Int,
    val name: String,
    val country: String,
    val temperature: String
)

data class WeatherResponse(
    val current: CurrentWeather
)

data class CurrentWeather(
    val temp_c: Double
)
