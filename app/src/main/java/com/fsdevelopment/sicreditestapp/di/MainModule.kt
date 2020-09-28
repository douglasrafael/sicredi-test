package com.fsdevelopment.sicreditestapp.di

import com.fsdevelopment.sicreditestapp.ui.event.EventViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModel = module {
    viewModel { EventViewModel(get()) }
}