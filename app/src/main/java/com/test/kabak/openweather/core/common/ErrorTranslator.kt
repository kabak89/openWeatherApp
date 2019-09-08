package com.test.kabak.openweather.core.common

import android.content.Context
import com.test.kabak.openweather.R
import com.test.kabak.openweather.core.common.exceptions.NetworkException

class ErrorTranslator {
    companion object {
        fun buildErrorMessage(exception: Exception, context: Context): String {
            val resources = context.resources

            if (exception is NetworkException) {
                return resources.getString(R.string.error_network)
            }

            return resources.getString(R.string.error_unknown)
        }
    }
}