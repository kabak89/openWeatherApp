package com.test.kabak.openweather.ui.forecast

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.test.kabak.openweather.R
import com.test.kabak.openweather.core.Resource
import com.test.kabak.openweather.databinding.ActivityForecastBinding
import com.test.kabak.openweather.presentation.list.CitiesListActivity.Companion.CITY_ID_KEY
import com.test.kabak.openweather.ui.common.BaseActivity
import com.test.kabak.openweather.ui.common.UniversalAdapter
import org.koin.androidx.viewmodel.ext.android.getViewModel

class ForecastActivity : BaseActivity<ActivityForecastBinding>(R.layout.activity_forecast) {
    private val viewModel: ForecastViewModel by lazy(mode = LazyThreadSafetyMode.NONE) {
        getViewModel()
    }
    private val citiesAdapter = UniversalAdapter()
    var cityId: String? = null

    init {
        citiesAdapter.addDelegate(DaysAdapterDelegate())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.forecastListView.adapter = citiesAdapter

        binding.forecastListView.setHasFixedSize(true)

        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.forecastListView.addItemDecoration(dividerItemDecoration)

        val extras = intent.extras
        cityId = extras!!.getString(CITY_ID_KEY)

        viewModel.getForecast(cityId).observe(this, Observer { listResource ->
            if (listResource == null) {
                return@Observer
            }

            if (listResource.status == Resource.COMPLETED) {
                if (listResource.exception == null) {
                    citiesAdapter.setItems(listResource.data!!.forecastDays)
                }
            }
        })
    }
}