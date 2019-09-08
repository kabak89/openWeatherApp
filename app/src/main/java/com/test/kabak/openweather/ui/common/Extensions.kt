package com.test.kabak.openweather.ui.common

import android.content.Context
import android.util.DisplayMetrics
import androidx.databinding.Observable
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.databinding.ObservableList
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import java.util.*

private val calendar = Calendar.getInstance()

fun <T : Observable> T.addOnPropertyChanged(callback: (T) -> Unit) =
        object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(observable: Observable?, i: Int) =
                    callback(observable as T)
        }.also { addOnPropertyChangedCallback(it) }.let {
            Disposables.fromAction { removeOnPropertyChangedCallback(it) }
        }

fun <T> ObservableList<T>.addOnListChangedCallback(callback: (ObservableList<T>) -> Unit): Disposable {
    val observer = object : ObservableList.OnListChangedCallback<ObservableList<T>>() {
        override fun onChanged(sender: ObservableList<T>?) {
            sender?.let {
                callback(sender)
            }
        }

        override fun onItemRangeRemoved(sender: ObservableList<T>?, positionStart: Int, itemCount: Int) {
            sender?.let {
                callback(sender)
            }
        }

        override fun onItemRangeMoved(sender: ObservableList<T>?, fromPosition: Int, toPosition: Int, itemCount: Int) {
            sender?.let {
                callback(sender)
            }
        }

        override fun onItemRangeInserted(sender: ObservableList<T>?, positionStart: Int, itemCount: Int) {
            sender?.let {
                callback(sender)
            }
        }

        override fun onItemRangeChanged(sender: ObservableList<T>?, positionStart: Int, itemCount: Int) {
            sender?.let {
                callback(sender)
            }
        }

    }

    addOnListChangedCallback(observer)

    return Disposables.fromAction { removeOnListChangedCallback(observer) }
}

fun <T> ObservableArrayList<T>.addOnArrayListChangedCallback(callback: (ObservableArrayList<T>) -> Unit): Disposable {
    val observer = object : ObservableList.OnListChangedCallback<ObservableArrayList<T>>() {
        override fun onChanged(sender: ObservableArrayList<T>?) {
            sender?.let {
                callback(sender)
            }
        }

        override fun onItemRangeRemoved(sender: ObservableArrayList<T>?, positionStart: Int, itemCount: Int) {
            sender?.let {
                callback(sender)
            }
        }

        override fun onItemRangeMoved(sender: ObservableArrayList<T>?, fromPosition: Int, toPosition: Int, itemCount: Int) {
            sender?.let {
                callback(sender)
            }
        }

        override fun onItemRangeInserted(sender: ObservableArrayList<T>?, positionStart: Int, itemCount: Int) {
            sender?.let {
                callback(sender)
            }
        }

        override fun onItemRangeChanged(sender: ObservableArrayList<T>?, positionStart: Int, itemCount: Int) {
            sender?.let {
                callback(sender)
            }
        }

    }

    addOnListChangedCallback(observer)

    return Disposables.fromAction { removeOnListChangedCallback(observer) }
}

fun Date.getAbsoluteDay(): Long {
    calendar.time = this

    val year = calendar.get(Calendar.YEAR)
    val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
    return (year * 1000 + dayOfYear).toLong()
}

fun Calendar.getAbsoluteDay(): Long {
    val year = this.get(Calendar.YEAR)
    val dayOfYear = this.get(Calendar.DAY_OF_YEAR)
    return (year * 1000 + dayOfYear).toLong()
}

fun Calendar.buildMonthStart(year: Int, month: Int): Date {
    set(Calendar.YEAR, year)
    set(Calendar.MONTH, month)
    set(Calendar.DAY_OF_MONTH, getMinimum(Calendar.DAY_OF_MONTH))
    return time
}

fun Calendar.buildMonthEnd(year: Int, month: Int): Date {
    set(Calendar.YEAR, year)
    set(Calendar.MONTH, month)
    set(Calendar.DAY_OF_MONTH, getMaximum(Calendar.DAY_OF_MONTH))
    return time
}

fun Calendar.buildDay(year: Int, month: Int, day: Int): Date {
    set(Calendar.YEAR, year)
    set(Calendar.MONTH, month)
    set(Calendar.DAY_OF_MONTH, day)
    return time
}

fun Calendar.buildTime(hour: Int, minute: Int, second: Int): Date {
    set(Calendar.HOUR_OF_DAY, hour)
    set(Calendar.MINUTE, minute)
    set(Calendar.SECOND, second)
    return time
}

fun Calendar.getYear(): Int {
    return get(Calendar.YEAR)
}

fun Calendar.getMonth(): Int {
    return get(Calendar.MONTH)
}

fun Calendar.getDay(): Int {
    return get(Calendar.DAY_OF_MONTH)
}

fun Context.pxToDp(px: Float): Float {
    val resources = resources
    val metrics = resources.displayMetrics
    val dp = px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    return dp
}

fun ObservableField<String>.getStringSafely(): String {
    val string = get()

    if (string == null) {
        return ""
    } else {
        return string
    }
}

/**
 * Replaces element at index with new element
 * @param element new element
 */
fun <T> MutableList<T>.replace(element: T, index: Int) {
    if (index > size - 1 || index < 0) {
        return
    }

    removeAt(index)
    add(index, element)
}