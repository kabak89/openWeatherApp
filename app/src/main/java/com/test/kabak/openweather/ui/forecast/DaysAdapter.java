package com.test.kabak.openweather.ui.forecast;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.test.kabak.openweather.core.storage.ForecastWeather;
import com.test.kabak.openweather.core.viewModels.ForecastDay;
import com.test.kabak.openweather.databinding.ViewForecastListDayBinding;

import java.util.ArrayList;
import java.util.List;

public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.DayBindingViewHolder>{
    List<ForecastDay> items = new ArrayList<>(10);

    public void setItems(@Nullable List<ForecastDay> cities) {
        this.items.clear();

        if (cities != null) {
            this.items.addAll(cities);
        }

        notifyDataSetChanged();
    }

    @Override
    public DayBindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewForecastListDayBinding binding = ViewForecastListDayBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        DayBindingViewHolder viewHolder = new DayBindingViewHolder(binding);

        RecyclerView.LayoutParams layoutParams
                = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);

        viewHolder.itemView.setLayoutParams(layoutParams);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DayBindingViewHolder holder, int position) {
        holder.getBinding().setItem(items.get(position));
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    static class DayBindingViewHolder extends RecyclerView.ViewHolder {
        private ViewForecastListDayBinding binding;

        public DayBindingViewHolder(ViewForecastListDayBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ViewForecastListDayBinding getBinding() {
            return binding;
        }
    }
}
