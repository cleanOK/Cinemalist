package com.dmytrod.cinemalist.presentation

import com.dmytrod.cinemalist.domain.*
import com.dmytrod.cinemalist.presentation.viewmodel.FavoriteMoviesViewModel
import com.dmytrod.cinemalist.presentation.viewmodel.BaseMoviesViewModel
import com.dmytrod.cinemalist.presentation.viewmodel.OngoingMoviesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val ONGOING = "ongoing"
const val FAVORITE = "favorite"
val presentationModule = module {
    viewModel<BaseMoviesViewModel>(named(ONGOING)) {
        OngoingMoviesViewModel(
            get(named(ONGOING_MOVIES)),
            get(named(MOVIES_BY_PAGE)),
            get(named(TOGGLE_FAVORITE)),
            get(named(REMOVE_MOVIES))
        )
    }
    viewModel<BaseMoviesViewModel>(named(FAVORITE)) {
        FavoriteMoviesViewModel(
            get(named(FAVORITE_MOVIES)),
            get(named(TOGGLE_FAVORITE))
        )
    }
}