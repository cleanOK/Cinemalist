package com.dmytrod.cinemalist.data.repository

import com.dmytrod.cinemalist.data.remote.model.RemoteError
import com.google.common.truth.Expect
import junit.framework.Assert.fail
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException
import java.lang.RuntimeException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ResponseHandlerTest {
    private lateinit var responseHandler: IResponseHandler

    @get:Rule
    val expect: Expect = Expect.create()

    @Before
    fun setUp() {
        responseHandler = ResponseHandler()
    }

    @Test
    fun `Should return NoInternet error when SocketTimeoutException`() {
        val exception = SocketTimeoutException()
        val expectedError = RemoteError.NoInternet(exception)
        testHandleException(exception, expectedError)
    }

    @Test
    fun `Should return NoInternet error when UnknownHostException`() {
        val exception = UnknownHostException()
        val expectedError = RemoteError.NoInternet(exception)
        testHandleException(exception, expectedError)
    }

    @Test
    fun `Should return ServerFailure error when IOException`() {
        val exception = IOException()
        val expectedError = RemoteError.ServerFailure(exception)
        testHandleException(exception, expectedError)
    }

    @Test
    fun `Should return Unexpected error for other cases`() {
        val exception = RuntimeException()
        val expectedError = RemoteError.Unexpected(exception)
        testHandleException(exception, expectedError)
    }

    @Test
    fun `Should return Success with data`() {
        val data = "Test object"
        val result = responseHandler.handleSuccess(data)
        expect.that(result).isEqualTo(IResponseHandler.Response.Success(data))
    }

    private fun testHandleException(exception: Throwable, expectedError: RemoteError) {
        val result = responseHandler.handleException<Any>(exception)
        if (result is IResponseHandler.Response.Error) {
            expect.that(result.remoteError).isEqualTo(expectedError)
        } else {
            fail("Result of handleException(Exception) is not an error")
        }
    }
}