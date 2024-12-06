package com.example.cityweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cityweather.adapter.CityAdapter
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var citiesRecyclerView: RecyclerView
    private lateinit var adapter: CityAdapter
    private lateinit var placeholderTextView: TextView
    private lateinit var progressBar: ProgressBar

    private lateinit var weatherViewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchEditText = findViewById(R.id.searchEditText)
        citiesRecyclerView = findViewById(R.id.citiesRecyclerView)
        placeholderTextView = findViewById(R.id.placeholderTextView)
        progressBar = findViewById(R.id.progressBar)
        weatherViewModel = ViewModelProvider(this)[WeatherViewModel::class.java]

        adapter = CityAdapter()
        citiesRecyclerView.layoutManager = LinearLayoutManager(this)
        citiesRecyclerView.adapter = adapter

        lifecycleScope.launch {
            weatherViewModel.cities.collect { cities ->
                adapter.updateCities(cities)
            }
        }

        lifecycleScope.launch {
            weatherViewModel.isLoading.collect { isLoading ->
                progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }

        lifecycleScope.launch {
            weatherViewModel.placeholderVisible.collect { visible ->
                placeholderTextView.visibility = if (visible) View.VISIBLE else View.GONE
            }
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                weatherViewModel.searchCities(query)
            }
        })
    }
}