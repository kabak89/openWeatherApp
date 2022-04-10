package com.example.mvvm.model

sealed class BaseViewEvent<out SE : Any> {
    data class ScreenEvent<SE : Any>(val event: SE) : BaseViewEvent<SE>()
}