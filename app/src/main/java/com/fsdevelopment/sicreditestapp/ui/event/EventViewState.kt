package com.fsdevelopment.sicreditestapp.ui.event

import androidx.annotation.StringRes
import com.fsdevelopment.sicreditestapp.data.model.Event

sealed class EventViewState {
    object RefreshData : EventViewState()

    class Loading(val isActive: Boolean) : EventViewState()
    class ShowData(val events: List<Event>) : EventViewState()
    class NoData(@StringRes val message: Int) : EventViewState()
    class Error(val message: Int) : EventViewState()
}
