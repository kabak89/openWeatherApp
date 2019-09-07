package com.test.kabak.openweather.ui.list;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.View;

import com.test.kabak.openweather.R;
import com.test.kabak.openweather.core.Resource;
import com.test.kabak.openweather.core.viewModels.CitiesListViewModel;
import com.test.kabak.openweather.core.viewModels.ListWeatherObject;
import com.test.kabak.openweather.databinding.ActivityListBinding;
import com.test.kabak.openweather.ui.BaseActivity;
import com.test.kabak.openweather.ui.addCity.AddCityActivity;
import com.test.kabak.openweather.ui.forecast.ForecastActivity;
import com.test.kabak.openweather.util.ListConfig;

import java.util.List;

import static com.test.kabak.openweather.core.Resource.COMPLETED;

public class ListActivity extends BaseActivity {
    public static final String CITY_ID_KEY = "CITY_ID_KEY";

    ActivityListBinding binding;
    CitiesAdapter citiesAdapter;
    CitiesListViewModel viewModel;
    ListResourceObserver observer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_list);
        binding.setHandler(this);

        citiesAdapter = new CitiesAdapter();
        citiesAdapter.setListener(new CitiesAdapter.CitiesAdapterListener() {
            @Override
            public void itemClicked(ListWeatherObject listWeatherObject) {
                Intent intent = new Intent(ListActivity.this, ForecastActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(CITY_ID_KEY, listWeatherObject.city.cityId);
                intent.putExtras(bundle);
                ActivityCompat.startActivity(ListActivity.this, intent, null);
            }
        });

        binding.setListConfig(new ListConfig(citiesAdapter));

        binding.refreshLayout.setEnabled(false);
        binding.tryAgainLabel.setVisibility(View.INVISIBLE);

        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.getListWeather().observe(ListActivity.this, observer);
            }
        });

        viewModel = ViewModelProviders.of(this).get(CitiesListViewModel.class);

        observer = new ListResourceObserver();
    }

    @Override
    protected void onStart() {
        super.onStart();

        viewModel.getListWeather().observe(this, observer);
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

    private class ListResourceObserver implements Observer<Resource<List<ListWeatherObject>>> {
        @Override
        public void onChanged(@Nullable Resource<List<ListWeatherObject>> listResource) {

            if(listResource == null) {
                return;
            }

            if(listResource.status == COMPLETED) {
                binding.refreshLayout.setRefreshing(false);

                if(listResource.exception != null) {
                    binding.tryAgainLabel.setVisibility(View.VISIBLE);
                    binding.refreshLayout.setEnabled(true);

                    if(listResource.data != null) {
                        citiesAdapter.setItems(listResource.data);
                    }
                } else {
                    citiesAdapter.setItems(listResource.data);
                    binding.tryAgainLabel.setVisibility(View.GONE);
                    binding.refreshLayout.setEnabled(false);
                }
            }
            else if(listResource.status == Resource.LOADING) {
                binding.refreshLayout.setRefreshing(true);
                binding.tryAgainLabel.setVisibility(View.GONE);
                binding.refreshLayout.setEnabled(false);
            }
        }
    }
}
