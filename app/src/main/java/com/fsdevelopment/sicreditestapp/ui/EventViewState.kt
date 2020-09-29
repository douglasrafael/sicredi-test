package com.fsdevelopment.sicreditestapp.ui

import com.fsdevelopment.sicreditestapp.data.model.Event

sealed class EventViewState {
    object NoData : EventViewState()

    class Loading(val isActive: Boolean) : EventViewState()
    class ShowData(val events: List<Event>) : EventViewState()
    class Error(val message: Int) : EventViewState()
    class SaveFavorite(val event: Event) : EventViewState()
    class RemoveFavorite(val event: Event) : EventViewState()
}
