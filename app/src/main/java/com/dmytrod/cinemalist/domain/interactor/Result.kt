package com.dmytrod.cinemalist.domain.interactor

import androidx.annotation.StringRes

sealed class Result<T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Failure<T>(@StringRes val errorMessageRes: Int, val e: Throwable) : Result<T>()
}