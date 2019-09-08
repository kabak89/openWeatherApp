package com.test.kabak.openweather.ui.common

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
    protected val scope = CoroutineScope(Dispatchers.IO)

    override fun onCleared() {
        scope.coroutineContext.cancel()
        scope.cancel()
        super.onCleared()
    }
}