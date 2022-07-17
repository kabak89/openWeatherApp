package com.test.kabak.openweather.ui.addCity

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.kabak.openweather.core.Resource
import com.test.kabak.openweather.core.repositories.AddCityRepository
import com.test.kabak.openweather.data.db.Storage
import com.test.kabak.openweather.data.db.entity.CityTable
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class AddCityViewModel : ViewModel() {
    private val storage: Storage by inject(Storage::class.java)

    var addCityRepository = AddCityRepository()
    var searchLiveData: LiveData<Resource<List<CityTable>>>? = null

    fun setSearchLiveData(): LiveData<Resource<List<CityTable>>> {
        if (searchLiveData == null) {
            searchLiveData = addCityRepository.searchLiveData
        }

        return searchLiveData!!
    }

    fun findCities(searchString: String?) {
        addCityRepository.findCities(searchString)
    }

    fun addCity(cityTable: CityTable) {
        viewModelScope.launch {
            storage.saveCity(cityTable)
        }
    }
}