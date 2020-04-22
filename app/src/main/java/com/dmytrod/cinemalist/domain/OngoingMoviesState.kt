package com.dmytrod.cinemalist.domain

import com.dmytrod.cinemalist.data.remote.model.RemoteError
import com.dmytrod.cinemalist.domain.entity.MovieEntity

sealed class OngoingMoviesState {
    data class Success(val totalPages: Int) : OngoingMoviesState()
    data class Error(val remoteError: RemoteError) : OngoingMoviesState()
    object Loading : OngoingMoviesState()
    object Empty : OngoingMoviesState()
}