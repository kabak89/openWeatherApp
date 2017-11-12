package com.test.kabak.openweather.ui;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

import com.test.kabak.openweather.util.ListConfig;

public class CommonBindingAdapters {
    @BindingAdapter("listConfig")
    public static void configRecyclerView(RecyclerView recyclerView, ListConfig config) {
        if (config == null) {
            return;
        }

        config.applyConfig(recyclerView.getContext(), recyclerView);
    }

//    @BindingAdapter("image")
//    public static void loadImage(ImageView imageView, String url) {
//        if (url == null) {
//            return;
//        }
//
//        Picasso
//                .with(imageView.getContext())
//                .load(Uri.parse(BASE_URL + url))
//                .fit()
//                .centerCrop()
//                .error(R.drawable.ic_broken_image_white_24px)
//                .into(imageView);
//    }
}
