package com.dmytrod.cinemalist.presentation

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { MovieViewModel(get(), get(), get(), get()) }
}