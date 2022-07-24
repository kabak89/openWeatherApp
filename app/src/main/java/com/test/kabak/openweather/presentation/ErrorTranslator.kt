package com.test.kabak.openweather.presentation

import android.content.Context
import com.test.kabak.openweather.R
import com.test.kabak.openweather.core.common.exceptions.NetworkException

class ErrorTranslator(
    private val context: Context,
) {
    fun buildErrorMessage(throwable: Throwable): String {
        val resources = context.resources

        if (throwable is NetworkException) {
            return resources.getString(R.string.error_network)
        }

        return resources.getString(R.string.error_unknown)
    }
}