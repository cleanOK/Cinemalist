package com.dmytrod.cinemalist.domain

import com.dmytrod.cinemalist.domain.interactor.FetchMoviesByPage
import com.dmytrod.cinemalist.domain.interactor.GetOngoingMovies
import com.dmytrod.cinemalist.domain.interactor.RemoveMoviesFromDB
import com.dmytrod.cinemalist.domain.interactor.ToggleFavoriteMovie
import org.koin.dsl.module

val domainModule = module {
    single { GetOngoingMovies(get()) }
    single { FetchMoviesByPage(get(), get()) }
    single { RemoveMoviesFromDB(get()) }
    single { ToggleFavoriteMovie(get()) }
}