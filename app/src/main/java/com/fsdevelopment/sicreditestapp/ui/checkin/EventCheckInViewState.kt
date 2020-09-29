package com.fsdevelopment.sicreditestapp.ui.checkin

import com.fsdevelopment.sicreditestapp.data.model.Event

sealed class EventCheckInViewState {
    object NoData : EventCheckInViewState()
    object Checked : EventCheckInViewState()

    class Loading(val isActive: Boolean) : EventCheckInViewState()
    class ShowData(val events: List<Event>) : EventCheckInViewState()
    class Error(val message: Int) : EventCheckInViewState()
}
