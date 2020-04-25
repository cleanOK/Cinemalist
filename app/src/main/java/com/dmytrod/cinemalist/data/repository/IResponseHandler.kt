package com.dmytrod.cinemalist.data.repository

import com.dmytrod.cinemalist.data.remote.model.RemoteError

interface IResponseHandler {
    fun <T : Any> handleException(e: Throwable): Response<T>
    fun <T : Any> handleSuccess(data: T): Response<T>

    sealed class Response<T> {
        data class Success<T>(val data: T) : Response<T>()
        data class Error<T>(val remoteError: RemoteError) : Response<T>()
    }
}