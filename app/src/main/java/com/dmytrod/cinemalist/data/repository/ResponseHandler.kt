package com.dmytrod.cinemalist.data.repository

import com.dmytrod.cinemalist.data.remote.model.RemoteError
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ResponseHandler {
    fun <T : Any> handleException(e: Throwable): Response<T> {
        return Response.Error(
            when (e) {
                is SocketTimeoutException, is UnknownHostException -> RemoteError.NoInternet
                is IOException -> RemoteError.ServerFailure
                //TODO add "is HttpException"
                else -> RemoteError.Unexpected
            }
        )
    }

    fun <T : Any> handleSuccess(data: T): Response<T> {
        return Response.Success(data)
    }

    sealed class Response<T> {
        data class Success<T>(val data: T) : Response<T>()
        data class Error<T>(val remoteError: RemoteError) : Response<T>()
    }

}