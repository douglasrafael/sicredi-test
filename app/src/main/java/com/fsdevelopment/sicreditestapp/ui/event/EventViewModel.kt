package com.fsdevelopment.sicreditestapp.ui.event

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fsdevelopment.sicreditestapp.data.repository.Repository
import com.fsdevelopment.sicreditestapp.data.repository.network.helpers.ResultWrapper
import com.fsdevelopment.sicreditestapp.utils.handlerHttpError
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class EventViewModel(private val repository: Repository) : ViewModel() {
    private var _eventStateView: MutableLiveData<EventViewState> = MutableLiveData()
    private var job = Job()

    fun getEventStateView(): MutableLiveData<EventViewState> = _eventStateView

    fun loadEvents(search: String? = null) {
        viewModelScope.launch {
            _eventStateView.value = EventViewState.Loading(true)

            when (val result = repository.listEvents(search)) {
                is ResultWrapper.Success -> _eventStateView.value = EventViewState
                    .ShowData(result.value)
                is ResultWrapper.Error -> _eventStateView.value = EventViewState.Error(
                    handlerHttpError(result.code)
                )
            }

            _eventStateView.value = EventViewState.Loading(false)
        }
    }

    fun onRefresh() {
        loadEvents()
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}