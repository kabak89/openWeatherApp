package com.test.kabak.openweather.ui.addCity

import android.os.Bundle
import android.text.Editable
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.test.kabak.openweather.R
import com.test.kabak.openweather.databinding.ActivityAddCityBinding
import com.test.kabak.openweather.ui.common.BaseActivity
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.util.concurrent.TimeUnit

class AddCityActivity : BaseActivity<ActivityAddCityBinding>(R.layout.activity_add_city) {
    private val searchAdapter = SearchAdapter()
    private var searchTextEmitter: ObservableEmitter<String>? = null
    private val addCityViewModel: AddCityViewModel by lazy(mode = LazyThreadSafetyMode.NONE) {
        getViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.handler = this

        searchAdapter.setListener { city ->
            addCityViewModel.addCity(city)
            onBackPressed()
        }

        binding.citiesListView.adapter = searchAdapter
        binding.citiesListView.setHasFixedSize(true)

        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.citiesListView.addItemDecoration(dividerItemDecoration)

        addCityViewModel
            .setSearchLiveData()
            .observe(this, Observer { listResource ->
                if (listResource?.data != null) {
                    searchAdapter.setItems(listResource.data)
                }
            })

        val disposable = Observable
            .create(ObservableOnSubscribe<String> { e -> searchTextEmitter = e })
            .debounce(500, TimeUnit.MILLISECONDS)
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { searchString -> addCityViewModel.findCities(searchString) }

        compositeDisposable.add(disposable)
    }

    fun afterTextChanged(s: Editable) {
        val searchString = s.toString().trim { it <= ' ' }
        searchTextEmitter?.onNext(searchString)
    }
}