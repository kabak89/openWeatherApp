package com.test.kabak.openweather.core.common

import com.test.kabak.openweather.core.common.exceptions.NetworkException
import com.test.kabak.openweather.core.common.exceptions.UnknownException
import retrofit2.HttpException
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


class ErrorConverter {
    companion object {
        fun convertError(e: Throwable): Exception {
            Timber.e(e)

            var result = Exception(e)

            val throwableToWork: Throwable

            if (e is RuntimeException && e.cause != null) {
                throwableToWork = e.cause!!
            } else {
                throwableToWork = e
            }

            try {
                when (throwableToWork) {
                    is UnknownHostException,
                    is SocketTimeoutException,
                    is ConnectException,
                    is HttpException,
                    is NetworkException -> {
                        result = NetworkException()
                    }
                }
            } catch (exception: Exception) {
                result = UnknownException()
            }

            return result
        }
    }
}