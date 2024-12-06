package com.example.cityweather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cityweather.api.ApiClient
import com.example.cityweather.model.CityWithWeather
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class WeatherViewModel : ViewModel() {

    private val _cities = MutableStateFlow<List<CityWithWeather>>(emptyList())
    val cities: StateFlow<List<CityWithWeather>> = _cities

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _placeholderVisible = MutableStateFlow(false)
    val placeholderVisible: StateFlow<Boolean> = _placeholderVisible

    private val apiService = ApiClient.apiService

    fun searchCities(query: String) {
        if (query.length < 2) {
            _cities.value = emptyList()
            _placeholderVisible.value = true
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val cities = apiService.searchCities(query, "4cd80af0c77740d3bfe54507231708")
                val citiesWithWeather = cities.map { city ->
                    async(Dispatchers.IO) {
                        val weather = apiService.getWeather("${city.name},${city.country}", "4cd80af0c77740d3bfe54507231708")
                        CityWithWeather(city.id, city.name, city.country, "${weather.current.temp_c}°C")
                    }
                }.awaitAll()
                _cities.value = citiesWithWeather
                _placeholderVisible.value = citiesWithWeather.isEmpty()
            } catch (e: Exception) {
                _cities.value = emptyList()
                _placeholderVisible.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }
}