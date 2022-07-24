package com.test.kabak.openweather.presentation

import com.test.kabak.openweather.core.common.exceptions.NetworkException
import retrofit2.HttpException
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ErrorConverter {
    fun convertError(e: Throwable): Throwable {
        Timber.e(e)

        return when (e) {
            is UnknownHostException, is SocketTimeoutException, is ConnectException,
            is HttpException, is NetworkException,
            -> {
                NetworkException()
            }
            else -> e
        }
    }
}