package com.example.cityweather.adapter

import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.cityweather.R
import com.example.cityweather.model.CityWithWeather

class CityAdapter : RecyclerView.Adapter<CityViewHolder>() {
    private val cities = mutableListOf<CityWithWeather>()

    fun updateCities(newCities: List<CityWithWeather>) {
        val diffResult = DiffUtil.calculateDiff(CityDiffCallback(cities, newCities))
        cities.clear()
        cities.addAll(newCities)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_city, parent, false)
        return CityViewHolder(view)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        holder.bind(cities[position])

        val fadeIn = ObjectAnimator.ofFloat(holder.itemView, "alpha", 0f, 1f)
        fadeIn.duration = 500
        fadeIn.start()
    }

    override fun getItemCount(): Int = cities.size
}

class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val cityName: TextView = itemView.findViewById(R.id.cityName)
    private val cityTemperature: TextView = itemView.findViewById(R.id.cityTemperature)

    fun bind(city: CityWithWeather) {
        cityName.text = "${city.name}, ${city.country}"
        cityTemperature.text = city.temperature
    }
}

class CityDiffCallback(
    private val oldList: List<CityWithWeather>,
    private val newList: List<CityWithWeather>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]
}
