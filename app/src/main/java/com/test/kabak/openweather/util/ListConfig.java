package com.test.kabak.openweather.util;

import android.content.Context;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ListConfig {
    private RecyclerView.Adapter adapter;

    public ListConfig(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    public void applyConfig(final Context context, final RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

        recyclerView.setHasFixedSize(true);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }
}
