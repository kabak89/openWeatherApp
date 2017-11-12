package com.test.kabak.openweather.ui.list;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.kabak.openweather.core.viewModels.ListWeatherObject;
import com.test.kabak.openweather.databinding.ViewCityListWeatherBinding;

import java.util.ArrayList;
import java.util.List;

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.CityWeatherBindingHolder> {
    public interface CitiesAdapterListener {
        void itemClicked(ListWeatherObject city);
    }

    List<ListWeatherObject> items = new ArrayList<>(10);
    CitiesAdapterListener listener;

    public CitiesAdapter() {
        super();
        setHasStableIds(true);
    }

    public void setItems(@Nullable List<ListWeatherObject> cities) {
        this.items.clear();

        if (cities != null) {
            this.items.addAll(cities);
        }

        notifyDataSetChanged();
    }

    public void setListener(CitiesAdapterListener listener) {
        this.listener = listener;
    }

    @Override
    public CityWeatherBindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewCityListWeatherBinding binding = ViewCityListWeatherBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        CityWeatherBindingHolder cityWeatherBindingHolder = new CityWeatherBindingHolder(binding);

        cityWeatherBindingHolder.itemView.setOnClickListener(new ItemOnClickListener(cityWeatherBindingHolder));

        RecyclerView.LayoutParams layoutParams
                = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);

        cityWeatherBindingHolder.itemView.setLayoutParams(layoutParams);

        return cityWeatherBindingHolder;
    }

    @Override
    public void onBindViewHolder(CityWeatherBindingHolder holder, int position) {
        holder.getBinding().setItem(items.get(position));
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).city.cityId.hashCode();
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    static class CityWeatherBindingHolder extends RecyclerView.ViewHolder {
        private ViewCityListWeatherBinding mBinding;

        public CityWeatherBindingHolder(ViewCityListWeatherBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public ViewCityListWeatherBinding getBinding() {
            return mBinding;
        }
    }

    private class ItemOnClickListener implements View.OnClickListener {
        CityWeatherBindingHolder cityWeatherBindingHolder;

        public ItemOnClickListener(CityWeatherBindingHolder cityWeatherBindingHolder) {
            this.cityWeatherBindingHolder = cityWeatherBindingHolder;
        }

        @Override
        public void onClick(View view) {
            if (listener == null) {
                return;
            }

            int position = cityWeatherBindingHolder.getAdapterPosition();
            listener.itemClicked(items.get(position));
        }
    }
}
