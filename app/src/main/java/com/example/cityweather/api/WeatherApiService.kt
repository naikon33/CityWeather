package com.example.cityweather.api

import com.example.cityweather.model.CityWithWeather
import com.example.cityweather.model.WeatherResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("search.json")
    suspend fun searchCities(
        @Query("q") query: String,
        @Query("key") apiKey: String
    ): List<CityWithWeather>

    @GET("current.json")
    suspend fun getWeather(
        @Query("q") location: String,
        @Query("key") apiKey: String
    ): WeatherResponse
}

object ApiClient {
    val apiService: WeatherApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.weatherapi.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiService::class.java)
    }
}
