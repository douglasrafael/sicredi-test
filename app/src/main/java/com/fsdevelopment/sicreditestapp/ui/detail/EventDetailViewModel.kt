package com.fsdevelopment.sicreditestapp.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fsdevelopment.sicreditestapp.data.model.Event
import com.fsdevelopment.sicreditestapp.data.repository.Repository
import com.mlykotom.valifi.fields.ValiFieldEmail
import kotlinx.coroutines.launch


/**
 * ViewModel user by [com.fsdevelopment.sicreditestapp.ui.detail.EventDetailFragment]
 */
class EventDetailViewModel(private val repository: Repository) : ViewModel() {
    private var _eventStateView: MutableLiveData<EventDetailViewState> = MutableLiveData()

    val email = ValiFieldEmail()


    fun getEventStateView(): MutableLiveData<EventDetailViewState> = _eventStateView

    /**
     * Save or Remove [event] from favorites.
     */
    fun saveRemoveFavorite(event: Event) {
        if (event.id.isNullOrEmpty()) return

        viewModelScope.launch {
            event.isFavorite = !event.isFavorite
            when (event.isFavorite) {
                true -> {
                    repository.saveFavorite(event.id)
                    _eventStateView.value = EventDetailViewState.SaveFavorite(event)
                }
                else -> {
                    repository.removeFavorite(event.id)
                    _eventStateView.value = EventDetailViewState.RemoveFavorite(event)
                }
            }
        }
    }
}