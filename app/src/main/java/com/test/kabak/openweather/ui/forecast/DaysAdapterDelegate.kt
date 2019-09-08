package com.test.kabak.openweather.ui.forecast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.test.kabak.openweather.databinding.ViewForecastListDayBinding
import com.test.kabak.openweather.ui.common.UniversalAdapter

class DaysAdapterDelegate: UniversalAdapter.UniversalAdapterDelegate<ForecastDay> {
    override fun getBinding(parent: ViewGroup, layoutInflater: LayoutInflater): ViewDataBinding? {
        return ViewForecastListDayBinding.inflate(layoutInflater, parent, false)
    }

    override fun onBindViewHolder(baseViewHolder: UniversalAdapter.BaseViewHolder, position: Int, item: ForecastDay) {
        (baseViewHolder.binding as ViewForecastListDayBinding).item = item
    }

    override fun canWorkWith(item: Any): Boolean {
       return item is ForecastDay
    }

    override fun getItemId(item: ForecastDay, position: Int): Long {
        return item.hashCode().toLong()
    }
}