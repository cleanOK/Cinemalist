package com.dmytrod.cinemalist.presentation

import com.dmytrod.cinemalist.domain.MOVIES_BY_PAGE
import com.dmytrod.cinemalist.domain.ONGOING_MOVIES
import com.dmytrod.cinemalist.domain.REMOVE_MOVIES
import com.dmytrod.cinemalist.domain.TOGGLE_FAVORITE
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val presentationModule = module {
    viewModel {
        MovieViewModel(
            get(named(ONGOING_MOVIES)),
            get(named(MOVIES_BY_PAGE)),
            get(named(TOGGLE_FAVORITE)),
            get(named(REMOVE_MOVIES))
        )
    }
}