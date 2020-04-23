package com.dmytrod.cinemalist.presentation

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.dmytrod.cinemalist.domain.entity.MovieEntity

sealed class OngoingMoviesState {
    object Success : OngoingMoviesState()
    object Loading : OngoingMoviesState()
    object Empty : OngoingMoviesState()
    //TODO add cause to handle errors differently, e.g. retry, ignore, etc.
    data class Error(@StringRes val errorMessageRes: Int, var isHandled: Boolean = false) :
        OngoingMoviesState()
}