package com.test.kabak.openweather.presentation.list

import com.test.kabak.openweather.util.PrintableText

sealed interface CitiesListEvent {
    data class ShowToast(val message: PrintableText) : CitiesListEvent
}