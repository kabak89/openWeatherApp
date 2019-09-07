package com.test.kabak.openweather.ui.forecast

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import com.test.kabak.openweather.R
import com.test.kabak.openweather.core.Resource
import com.test.kabak.openweather.core.viewModels.ForecastViewModel
import com.test.kabak.openweather.databinding.ActivityForecastBinding
import com.test.kabak.openweather.ui.common.BaseActivity
import com.test.kabak.openweather.ui.list.ListActivity.Companion.CITY_ID_KEY

class ForecastActivity : BaseActivity<ActivityForecastBinding>(R.layout.activity_forecast) {
    private lateinit var viewModel: ForecastViewModel
    private val citiesAdapter = DaysAdapter()
    var cityId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.forecastListView.adapter = citiesAdapter

        binding.forecastListView.setHasFixedSize(true)

        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.forecastListView.addItemDecoration(dividerItemDecoration)

        viewModel = ViewModelProviders.of(this).get(ForecastViewModel::class.java)

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
