package com.dmytrod.cinemalist.data.repository

import com.dmytrod.cinemalist.data.remote.model.RemoteError
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ResponseHandler : IResponseHandler {
    override fun <T : Any> handleException(e: Throwable): IResponseHandler.Response<T> {
        return IResponseHandler.Response.Error(
            when (e) {
                is SocketTimeoutException, is UnknownHostException -> RemoteError.NoInternet(e)
                is IOException -> RemoteError.ServerFailure(e)
                else -> RemoteError.Unexpected(e)
            }
        )
    }

    override fun <T : Any> handleSuccess(data: T): IResponseHandler.Response<T> {
        return IResponseHandler.Response.Success(data)
    }

}