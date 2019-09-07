package com.test.kabak.openweather.ui.forecast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.test.kabak.openweather.R;
import com.test.kabak.openweather.core.Resource;
import com.test.kabak.openweather.core.viewModels.ForecastDayObject;
import com.test.kabak.openweather.core.viewModels.ForecastViewModel;
import com.test.kabak.openweather.databinding.ActivityForecastBinding;
import com.test.kabak.openweather.ui.BaseActivity;
import com.test.kabak.openweather.util.ListConfig;

import static com.test.kabak.openweather.ui.list.ListActivity.CITY_ID_KEY;

public class ForecastActivity extends BaseActivity{
    ForecastViewModel viewModel;
    DaysAdapter citiesAdapter;
    String cityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityForecastBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_forecast);

        citiesAdapter = new DaysAdapter();
        binding.setListConfig(new ListConfig(citiesAdapter));

        viewModel = ViewModelProviders.of(this).get(ForecastViewModel.class);

        Bundle extras = getIntent().getExtras();
        cityId = extras.getString(CITY_ID_KEY);

        viewModel.getForecast(cityId).observe(this, new Observer<Resource<ForecastDayObject>>() {
            @Override
            public void onChanged(@Nullable Resource<ForecastDayObject> listResource) {
                if(listResource == null) {
                    return;
                }

                if(listResource.status == Resource.COMPLETED) {
                    if(listResource.exception == null) {
                        citiesAdapter.setItems(listResource.data.forecastDays);
                    }
                }
            }
        });
    }
}
