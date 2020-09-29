package com.fsdevelopment.sicreditestapp.ui.checkin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fsdevelopment.sicreditestapp.R
import com.fsdevelopment.sicreditestapp.data.model.CheckIn
import com.fsdevelopment.sicreditestapp.data.model.Event
import com.fsdevelopment.sicreditestapp.data.repository.Repository
import com.fsdevelopment.sicreditestapp.data.repository.network.helpers.ResultWrapper
import com.fsdevelopment.sicreditestapp.utils.handlerHttpError
import com.mlykotom.valifi.ValiFiForm
import com.mlykotom.valifi.fields.ValiFieldEmail
import com.mlykotom.valifi.fields.ValiFieldText
import kotlinx.coroutines.launch

/**
 * ViewModel user by [com.fsdevelopment.sicreditestapp.ui.checkin.EventCheckInListFragment]
 * and [com.fsdevelopment.sicreditestapp.ui.checkin.EventCheckInRegisterFragment]
 */
class EventCheckInViewModel(private val repository: Repository) : ViewModel() {
    private var _eventStateView: MutableLiveData<EventCheckInViewState> = MutableLiveData()

    val name: ValiFieldText = ValiFieldText()
        .addMinLengthValidator(R.string.message_name_required, 10)
    val email: ValiFieldText = ValiFieldEmail(R.string.message_email_required)
    val form = ValiFiForm(name, email)

    fun getEventStateView(): MutableLiveData<EventCheckInViewState> = _eventStateView

    fun loadCheckedEvents() {
        viewModelScope.launch {
            _eventStateView.value = EventCheckInViewState.Loading(true)

            when (val result = repository.listCheckInEvents()) {
                is ResultWrapper.Success -> handlerResultSuccess(result.value)
                is ResultWrapper.Error -> handlerResultError(result.code)
            }

            _eventStateView.value = EventCheckInViewState.Loading(false)
        }
    }

    fun onSaveCheckIn(eventId: String) {
        if (!form.isValid) return

        viewModelScope.launch {
            _eventStateView.value = EventCheckInViewState.Loading(true)

            val checkIn = CheckIn(name.value.toString(), email.value.toString(), eventId)
            when (val result = repository.checkIn(checkIn)) {
                is ResultWrapper.Success -> _eventStateView.value = EventCheckInViewState.Checked
                is ResultWrapper.Error -> handlerResultError(result.code)
            }

            _eventStateView.value = EventCheckInViewState.Loading(false)
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
            true -> _eventStateView.value = EventCheckInViewState.NoData
            else -> _eventStateView.value = EventCheckInViewState.ShowData(events)
        }
    }

    /**
     * Handle request error.
     */
    private fun handlerResultError(code: Int?) {
        _eventStateView.value = EventCheckInViewState.Error(handlerHttpError(code))
    }
}