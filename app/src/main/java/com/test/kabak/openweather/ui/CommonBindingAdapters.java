package com.test.kabak.openweather.ui;

import android.databinding.BindingAdapter;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.test.kabak.openweather.R;
import com.test.kabak.openweather.util.ListConfig;

import static com.test.kabak.openweather.core.network.ServerApi.IMAGES_BASE_URL;

public class CommonBindingAdapters {
    @BindingAdapter("listConfig")
    public static void configRecyclerView(RecyclerView recyclerView, ListConfig config) {
        if (config == null) {
            return;
        }

        config.applyConfig(recyclerView.getContext(), recyclerView);
    }

    @BindingAdapter("weatherIcon")
    public static void loadImage(ImageView imageView, String url) {
        if (url == null) {
            return;
        }

        Picasso
                .with(imageView.getContext())
                .load(Uri.parse(IMAGES_BASE_URL + url + ".png"))
                .fit()
                .centerCrop()
                .error(R.drawable.ic_broken_image_white_24px)
                .into(imageView);
    }
}
