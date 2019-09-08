package com.test.kabak.openweather.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.test.kabak.openweather.databinding.ViewCityListWeatherBinding
import com.test.kabak.openweather.ui.common.UniversalAdapter

class CitiesAdapterDelegate : UniversalAdapter.UniversalAdapterDelegate<ListWeatherObject> {
    override fun getBinding(parent: ViewGroup, layoutInflater: LayoutInflater): ViewDataBinding? {
        return ViewCityListWeatherBinding.inflate(layoutInflater, parent, false)
    }

    override fun onBindViewHolder(baseViewHolder: UniversalAdapter.BaseViewHolder, position: Int, item: ListWeatherObject) {
        (baseViewHolder.binding as ViewCityListWeatherBinding).item = item
    }

    override fun canWorkWith(item: Any): Boolean {
        return item is ListWeatherObject
    }

    override fun getItemId(item: ListWeatherObject, position: Int): Long {
        return item.city.cityId.hashCode().toLong()
    }
}