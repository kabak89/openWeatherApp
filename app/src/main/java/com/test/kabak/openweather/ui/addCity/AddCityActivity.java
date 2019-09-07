package com.test.kabak.openweather.ui.addCity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.Editable;

import com.test.kabak.openweather.R;
import com.test.kabak.openweather.core.Resource;
import com.test.kabak.openweather.core.storage.City;
import com.test.kabak.openweather.core.viewModels.AddCityViewModel;
import com.test.kabak.openweather.databinding.ActivityAddCityBinding;
import com.test.kabak.openweather.ui.BaseActivity;
import com.test.kabak.openweather.util.ListConfig;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AddCityActivity extends BaseActivity {
    ActivityAddCityBinding binding;
    AddCityViewModel addCityViewModel;
    SearchAdapter searchAdapter;
    ObservableEmitter<String> searchTextEmitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_city);
        binding.setHandler(this);

        searchAdapter = new SearchAdapter();

        searchAdapter.setListener(new SearchAdapter.SearchAdapterListener() {
            @Override
            public void itemClicked(City city) {
                addCityViewModel
                        .addCity(city)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action() {
                            @Override
                            public void run() throws Exception {
                                AddCityActivity.this.onBackPressed();
                            }
                        });
            }
        });

        binding.setListConfig(new ListConfig(searchAdapter));

        addCityViewModel = ViewModelProviders.of(this).get(AddCityViewModel.class);

        addCityViewModel
                .setSearchLiveData()
                .observe(this, new Observer<Resource<List<City>>>() {
                    @Override
                    public void onChanged(@Nullable Resource<List<City>> listResource) {
                        if(listResource != null && listResource.data != null) {
                            searchAdapter.setItems(listResource.data);
                        }
                    }
                });

        Observable
                .create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                        searchTextEmitter = e;
                    }
                })
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String searchString) throws Exception {
                        addCityViewModel.findCities(searchString);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        binding = null;
        super.onDestroy();
    }

    public void afterTextChanged(Editable s) {
        String searchString = s.toString().trim();
        searchTextEmitter.onNext(searchString);
    }
}
