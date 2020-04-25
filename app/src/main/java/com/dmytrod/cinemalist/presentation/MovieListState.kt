package com.dmytrod.cinemalist.presentation

import androidx.annotation.StringRes

sealed class MovieListState {
    object Success : MovieListState()
    object Loading : MovieListState()
    object Empty : MovieListState()
    //TODO add cause to handle errors differently, e.g. retry, ignore, etc.
    data class Error(@StringRes val errorMessageRes: Int, var isHandled: Boolean = false) :
        MovieListState()
}