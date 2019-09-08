package com.test.kabak.openweather.core.common

import android.app.Activity
import android.view.View
import androidx.appcompat.app.AlertDialog

class ErrorInteractor {
    interface NonCriticalErrorHandler {
        fun handleNonCriticalError(exception: Exception, activity: Activity, rootView: View)
    }

    companion object {
        private val defaultNonCriticalErrorHandler = DefaultNonCriticalErrorHandler()

        private fun handleCriticalError(exception: Exception, activity: Activity, rootView: View): Boolean {
            return false
        }

        fun handleError(exception: Exception, activity: Activity, rootView: View) {
            handleError(exception, activity, rootView, defaultNonCriticalErrorHandler)
        }

        fun handleError(exception: Exception, activity: Activity, rootView: View, nonCriticalHandler: NonCriticalErrorHandler) {
            val isEverythingBad = handleCriticalError(exception, activity, rootView)

            if (!isEverythingBad) {
                nonCriticalHandler.handleNonCriticalError(exception, activity, rootView)
            }
        }
    }

    open class DefaultNonCriticalErrorHandler : NonCriticalErrorHandler {
        override fun handleNonCriticalError(exception: Exception, activity: Activity, rootView: View) {
            val errorMessage = ErrorTranslator.buildErrorMessage(exception, activity)
            val builder = AlertDialog.Builder(activity)

            builder
                    .setMessage(errorMessage)
                    .setPositiveButton(android.R.string.ok) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setCancelable(false)
                    .show()
        }
    }
}