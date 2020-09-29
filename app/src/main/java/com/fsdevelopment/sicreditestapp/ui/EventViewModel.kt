package com.fsdevelopment.sicreditestapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fsdevelopment.sicreditestapp.data.model.Event
import com.fsdevelopment.sicreditestapp.data.repository.Repository
import com.fsdevelopment.sicreditestapp.data.repository.network.helpers.ResultWrapper
import com.fsdevelopment.sicreditestapp.utils.handlerHttpError
import kotlinx.coroutines.launch

/**
 * ViewModel shared with modules involving listing [com.fsdevelopment.sicreditestapp.ui.list.EventListFragment]
 * and [com.fsdevelopment.sicreditestapp.ui.search.EventSearchFragment]
 */
class EventViewModel(private val repository: Repository) : ViewModel() {
    private var _eventStateView: MutableLiveData<EventViewState> = MutableLiveData()

    fun getEventStateView(): MutableLiveData<EventViewState> = _eventStateView

    /**
     * Retrieves list of events based on the [search] term.
     * When the search value is null, all events are returned.
     */
    fun loadEvents(search: String? = null) {
        viewModelScope.launch {
            _eventStateView.value = EventViewState.Loading(true)

            when (val result = repository.listEvents(search)) {
                is ResultWrapper.Success -> handlerResultSuccess(result.value)
                is ResultWrapper.Error -> handlerResultError(result.code)
            }

            _eventStateView.value = EventViewState.Loading(false)
        }
    }

    /**
     * Handle request successfully.
     *
     * When there are no [events] or the term resulted in nothing found,
     * the No Data status is emitted.
     */
    private fun handlerResultSuccess(events: List<Event>) {
        when (events.isEmpty()) {
            true -> _eventStateView.value = EventViewState.NoData
            else -> _eventStateView.value = EventViewState.ShowData(events)
        }
    }

    /**
     * Handle request error.
     */
    private fun handlerResultError(code: Int?) {
        _eventStateView.value = EventViewState.Error(handlerHttpError(code))
    }
}