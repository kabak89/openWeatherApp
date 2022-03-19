package com.test.kabak.openweather.ui.list

import android.content.Intent
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import com.test.kabak.openweather.R
import com.test.kabak.openweather.core.common.ErrorInteractor
import com.test.kabak.openweather.databinding.ActivityListBinding
import com.test.kabak.openweather.ui.addCity.AddCityActivity
import com.test.kabak.openweather.ui.common.BaseActivity
import com.test.kabak.openweather.ui.common.UniversalAdapter
import com.test.kabak.openweather.ui.common.addOnArrayListChangedCallback
import com.test.kabak.openweather.ui.forecast.ForecastActivity

class ListActivity : BaseActivity<ActivityListBinding>(R.layout.activity_list) {
    private val citiesAdapter = UniversalAdapter()
    private lateinit var viewModel: CitiesListViewModel

    init {
        citiesAdapter.addDelegate(CitiesAdapterDelegate())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(CitiesListViewModel::class.java)

        binding.viewModel = viewModel

        binding.citiesListView.adapter = citiesAdapter
        binding.citiesListView.setHasFixedSize(true)

        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.citiesListView.addItemDecoration(dividerItemDecoration)

        viewModel.stateLiveData.observe(this) { state ->
            state?.let {
                citiesAdapter.setItems(state.cities)

                state.cities.addOnArrayListChangedCallback {
                    citiesAdapter.setItems(state.cities)
                }
            }
        }

        viewModel.errorsLiveData.observe(this) { event ->
            event?.getContentIfNotHandled()?.let {
                ErrorInteractor.handleError(it, this@ListActivity, binding.root)
            }
        }

        viewModel.goAddCityLiveData.observe(this) { event ->
            event?.getContentIfNotHandled()?.let {
                val intent = Intent(this@ListActivity, AddCityActivity::class.java)
                ActivityCompat.startActivity(this@ListActivity, intent, null)
            }
        }

        viewModel.goCityDetailsLiveData.observe(this) { event ->
            event?.getContentIfNotHandled()?.let { listWeatherObject ->
                val intent = Intent(this@ListActivity, ForecastActivity::class.java)
                val bundle = Bundle()
                bundle.putString(CITY_ID_KEY, listWeatherObject.city.cityId)
                intent.putExtras(bundle)
                ActivityCompat.startActivity(this@ListActivity, intent, null)
            }
        }
    }

    override fun onStart() {
        super.onStart()

        viewModel.loadData()
    }

    companion object {
        const val CITY_ID_KEY = "CITY_ID_KEY"
    }
}
