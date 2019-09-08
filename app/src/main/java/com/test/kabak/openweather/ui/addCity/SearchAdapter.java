package com.test.kabak.openweather.ui.addCity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.test.kabak.openweather.core.storage.City;
import com.test.kabak.openweather.databinding.ViewSearchResultBinding;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.CityBindingHolder> {
    public interface SearchAdapterListener {
        void itemClicked(City city);
    }

    List<City> items = new ArrayList<>(10);
    SearchAdapterListener listener;

    public SearchAdapter() {
        super();
        setHasStableIds(true);
    }

    public void setItems(@Nullable List<City> cities) {
        this.items.clear();

        if (cities != null) {
            this.items.addAll(cities);
        }

        notifyDataSetChanged();
    }

    public void setListener(SearchAdapterListener listener) {
        this.listener = listener;
    }

    @Override
    public CityBindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewSearchResultBinding binding = ViewSearchResultBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        CityBindingHolder cityBindingHolder = new CityBindingHolder(binding);
        cityBindingHolder.itemView.setOnClickListener(new ItemOnClickListener(cityBindingHolder));
        return cityBindingHolder;
    }

    @Override
    public void onBindViewHolder(CityBindingHolder holder, int position) {
        holder.getBinding().setItem(items.get(position));
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getCityId().hashCode();
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    static class CityBindingHolder extends RecyclerView.ViewHolder {
        private ViewSearchResultBinding mBinding;

        public CityBindingHolder(ViewSearchResultBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public ViewSearchResultBinding getBinding() {
            return mBinding;
        }
    }

    private class ItemOnClickListener implements View.OnClickListener {
        CityBindingHolder cityBindingHolder;

        public ItemOnClickListener(CityBindingHolder cityBindingHolder) {
            this.cityBindingHolder = cityBindingHolder;
        }

        @Override
        public void onClick(View view) {
            if (listener == null) {
                return;
            }

            int position = cityBindingHolder.getAdapterPosition();
            listener.itemClicked(items.get(position));
        }
    }
}
