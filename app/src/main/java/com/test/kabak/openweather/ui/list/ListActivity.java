package com.test.kabak.openweather.ui.list;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.test.kabak.openweather.R;
import com.test.kabak.openweather.core.viewModels.CitiesListViewModel;
import com.test.kabak.openweather.core.viewModels.ListWeatherObject;
import com.test.kabak.openweather.databinding.ActivityListBinding;
import com.test.kabak.openweather.ui.addCity.AddCityActivity;
import com.test.kabak.openweather.util.ListConfig;

import java.util.List;

public class ListActivity extends AppCompatActivity {
    ActivityListBinding binding;
    CitiesAdapter citiesAdapter;
    CitiesListViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_list);
        binding.setHandler(this);

        citiesAdapter = new CitiesAdapter();
        binding.setListConfig(new ListConfig(citiesAdapter));

        viewModel = ViewModelProviders.of(this).get(CitiesListViewModel.class);

        viewModel.weatherLiveData.observe(this, new Observer<List<ListWeatherObject>>() {
            @Override
            public void onChanged(@Nullable List<ListWeatherObject> currentWeathers) {
                citiesAdapter.setItems(currentWeathers);
            }
        });
    }

    @Override
    protected void onDestroy() {
        binding = null;
        super.onDestroy();
    }

    public void addCityClick(View v) {
        Intent intent = new Intent(this, AddCityActivity.class);
        ActivityCompat.startActivity(this, intent, null);
    }
}
