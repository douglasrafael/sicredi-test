package com.fsdevelopment.sicreditestapp.di

import com.fsdevelopment.sicreditestapp.ui.EventViewModel
import com.fsdevelopment.sicreditestapp.ui.checkin.EventCheckInViewModel
import com.fsdevelopment.sicreditestapp.ui.detail.EventDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModel = module {
    viewModel { EventViewModel(get()) }
    viewModel { EventDetailViewModel(get()) }
    viewModel { EventCheckInViewModel(get()) }
}