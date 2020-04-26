package com.dmytrod.cinemalist.data.repository

import com.dmytrod.cinemalist.data.remote.TMDBApiService
import com.dmytrod.cinemalist.data.remote.model.MoviesResponse
import com.dmytrod.cinemalist.data.remote.model.RemoteError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.internal.verification.VerificationModeFactory.times
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.Mockito.`when` as mockitoWhen

@RunWith(MockitoJUnitRunner::class)
class RemoteRepositoryTest {
    @Mock
    private lateinit var service: TMDBApiService
    private val apiKey = "test api key"

    @Mock
    private lateinit var responseHandler: IResponseHandler
    private lateinit var remoteRepository: IRemoteRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        remoteRepository = RemoteRepository(service, apiKey, responseHandler)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Should call handleException when service throws one`() =
        runBlockingTest {
            val exception = RuntimeException()
            val page = 4
            val expected =
                IResponseHandler.Response.Error<MoviesResponse>(RemoteError.Unexpected(exception))
            mockitoWhen(service.getOngoingMovies(page = page, apiKey = apiKey))
                .thenThrow(exception)
            mockitoWhen(responseHandler.handleException<MoviesResponse>(exception))
                .thenReturn(expected)
            remoteRepository.getOngoingMovies(page)
            Mockito.verify(responseHandler, times(1)).handleException<MoviesResponse>(exception)
        }

    @ExperimentalCoroutinesApi
    @Test
    fun `Should call handleSuccess when service returns result`() =
        runBlockingTest {
            val result = mock(MoviesResponse::class.java)
            val page = 4
            mockitoWhen(service.getOngoingMovies(page = page, apiKey = apiKey))
                .thenReturn(result)
            mockitoWhen(responseHandler.handleSuccess<MoviesResponse>(result))
                .thenReturn(IResponseHandler.Response.Success(result))
            remoteRepository.getOngoingMovies(page)
            Mockito.verify(responseHandler, times(1)).handleSuccess<MoviesResponse>(result)
        }

}
