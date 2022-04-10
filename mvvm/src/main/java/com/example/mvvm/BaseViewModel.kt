package com.example.mvvm

import androidx.lifecycle.ViewModel
import com.example.mvvm.model.BaseViewEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

abstract class BaseViewModel<S : Any, E : Any> : ViewModel() {
    private val _viewState by lazy { MutableStateFlow(createInitialState()) }
    val viewState get() = _viewState.asStateFlow()

    private val _viewEvent = Channel<BaseViewEvent<E>>(Channel.UNLIMITED)
    val viewEvent = _viewEvent.receiveAsFlow()

    protected val currentViewState: S get() = viewState.value

    protected abstract fun createInitialState(): S

    protected fun updateState(update: (S) -> S) {
        _viewState.update(update)
    }

    protected fun sendEvent(event: BaseViewEvent<E>) {
        _viewEvent.trySend(event)
    }

    protected fun sendEvent(event: E) {
        sendEvent(BaseViewEvent.ScreenEvent(event))
    }
}