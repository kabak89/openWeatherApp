package com.test.kabak.openweather.ui.list

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import com.test.kabak.openweather.R
import com.test.kabak.openweather.core.Resource
import com.test.kabak.openweather.core.Resource.COMPLETED
import com.test.kabak.openweather.databinding.ActivityListBinding
import com.test.kabak.openweather.ui.addCity.AddCityActivity
import com.test.kabak.openweather.ui.common.BaseActivity
import com.test.kabak.openweather.ui.forecast.ForecastActivity

class ListActivity : BaseActivity<ActivityListBinding>(R.layout.activity_list) {
    private val citiesAdapter = CitiesAdapter()
    private lateinit var viewModel: CitiesListViewModel
    private val observer = ListResourceObserver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.handler = this

        citiesAdapter.setListener { listWeatherObject ->
            val intent = Intent(this@ListActivity, ForecastActivity::class.java)
            val bundle = Bundle()
            bundle.putString(CITY_ID_KEY, listWeatherObject.city.cityId)
            intent.putExtras(bundle)
            ActivityCompat.startActivity(this@ListActivity, intent, null)
        }

        binding.citiesListView.adapter = citiesAdapter

        binding.citiesListView.setHasFixedSize(true)

        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.citiesListView.addItemDecoration(dividerItemDecoration)

        binding.refreshLayout.isEnabled = false
        binding.tryAgainLabel.visibility = View.INVISIBLE

        binding.refreshLayout.setOnRefreshListener { viewModel.listWeather.observe(this@ListActivity, observer) }

        viewModel = ViewModelProviders.of(this).get(CitiesListViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()

        viewModel.listWeather.observe(this, observer)
    }

    fun addCityClick(v: View) {
        val intent = Intent(this, AddCityActivity::class.java)
        ActivityCompat.startActivity(this, intent, null)
    }

    private inner class ListResourceObserver : Observer<Resource<List<ListWeatherObject>>> {
        override fun onChanged(listResource: Resource<List<ListWeatherObject>>?) {

            if (listResource == null) {
                return
            }

            if (listResource.status == COMPLETED) {
                binding.refreshLayout.isRefreshing = false

                if (listResource.exception != null) {
                    binding.tryAgainLabel.visibility = View.VISIBLE
                    binding.refreshLayout.isEnabled = true

                    if (listResource.data != null) {
                        citiesAdapter.setItems(listResource.data)
                    }
                } else {
                    citiesAdapter.setItems(listResource.data)
                    binding.tryAgainLabel.visibility = View.GONE
                    binding.refreshLayout.isEnabled = false
                }
            } else if (listResource.status == Resource.LOADING) {
                binding.refreshLayout.isRefreshing = true
                binding.tryAgainLabel.visibility = View.GONE
                binding.refreshLayout.isEnabled = false
            }
        }
    }

    companion object {
        val CITY_ID_KEY = "CITY_ID_KEY"
    }
}
