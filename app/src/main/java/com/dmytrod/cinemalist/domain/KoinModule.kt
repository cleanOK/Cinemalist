package com.dmytrod.cinemalist.domain

import com.dmytrod.cinemalist.domain.entity.MovieEntity
import com.dmytrod.cinemalist.domain.interactor.*
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val ONGOING_MOVIES = "ongoing_movies"
const val FAVORITE_MOVIES = "favorite_movies"
const val MOVIES_BY_PAGE = "movies_by_page"
const val REMOVE_MOVIES = "remove_movies"
const val TOGGLE_FAVORITE = "toggle_favorite"
val domainModule = module {
    single<DataSourceFactoryInteractor<MovieEntity>>(named(ONGOING_MOVIES)) { GetOngoingMovies(get()) }
    single<DataSourceFactoryInteractor<MovieEntity>>(named(FAVORITE_MOVIES)) { GetFavoriteMovies(get()) }
    single<FlowInteractor<Int, PageData>>(named(MOVIES_BY_PAGE)) { FetchMoviesByPage(get(), get()) }
    single<FlowInteractor<Unit, Unit>>(named(REMOVE_MOVIES)) { RemoveMoviesFromDB(get()) }
    single<FlowInteractor<MovieEntity, Unit>>(named(TOGGLE_FAVORITE)) { ToggleFavoriteMovie(get()) }
}