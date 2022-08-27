package com.test.kabak.openweather.ui

import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import com.test.kabak.openweather.R
import com.test.kabak.openweather.core.network.Constants.IMAGES_BASE_URL
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("weatherIcon")
fun loadImage(imageView: ImageView, url: String?) {
    if (url == null) {
        return
    }

    Picasso
            .get()
            .load(Uri.parse("$IMAGES_BASE_URL$url.png"))
            .fit()
            .centerCrop()
            .error(R.drawable.ic_broken_image_white_24px)
            .into(imageView)
}

@BindingAdapter("monthDay")
fun setMonthDayTimestamp(textView: TextView, timestamp: Long) {
    val simpleDateFormat = SimpleDateFormat("d LLLL", Locale.getDefault())
    textView.text = simpleDateFormat.format(Date(timestamp))
}

@BindingAdapter("weekDay")
fun setWeekDayTimestamp(textView: TextView, timestamp: Long) {
    val simpleDateFormat = SimpleDateFormat("EEEE", Locale.getDefault())
    textView.text = simpleDateFormat.format(Date(timestamp))
}