package com.dmytrod.cinemalist.presentation.viewmodel

import com.dmytrod.cinemalist.domain.entity.MovieEntity
import com.dmytrod.cinemalist.domain.interactor.DataSourceFactoryInteractor
import com.dmytrod.cinemalist.domain.interactor.FlowInteractor

class FavoriteMoviesViewModel(
    getMovies: DataSourceFactoryInteractor<MovieEntity>,
    toggleFavoriteMovie: FlowInteractor<MovieEntity, Unit>
) : BaseMoviesViewModel(getMovies, toggleFavoriteMovie) {
    override fun refreshMovieList() {
        //Do nothing
    }
}