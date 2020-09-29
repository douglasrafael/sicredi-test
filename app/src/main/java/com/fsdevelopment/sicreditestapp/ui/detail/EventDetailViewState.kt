package com.fsdevelopment.sicreditestapp.ui.detail

import com.fsdevelopment.sicreditestapp.data.model.Event

sealed class EventDetailViewState {
    class SaveFavorite(val event: Event) : EventDetailViewState()
    class RemoveFavorite(val event: Event) : EventDetailViewState()
}
