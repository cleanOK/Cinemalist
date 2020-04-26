package com.dmytrod.cinemalist.domain.interactor

import com.dmytrod.cinemalist.R
import com.dmytrod.cinemalist.data.repository.PersistenceRepository
import com.dmytrod.cinemalist.domain.entity.MovieEntity
import com.dmytrod.cinemalist.test
import com.google.common.truth.Expect
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.Mockito.`when` as mockitoWhen

class ToggleFavoriteMovieTest {
    @Mock
    private lateinit var persistenceRepository: PersistenceRepository
    private lateinit var toggleFavoriteMovie: ToggleFavoriteMovie

    @get:Rule
    val expect: Expect = Expect.create()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        toggleFavoriteMovie = ToggleFavoriteMovie(persistenceRepository)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Should return Success when toggle successful`() = runBlockingTest {
        val movie = mock(MovieEntity::class.java)
        mockitoWhen(persistenceRepository.toggleFavoriteMovie(movie))
            .thenReturn(Unit)
        toggleFavoriteMovie.execute(movie)
            .test(this)
            .assertValues(Result.Success<Unit>(Unit))
            .finish()
    }


    @ExperimentalCoroutinesApi
    @Test
    fun `Should return Failure when removing from favorites throws exception`()  {
        val expectedMessage = R.string.failed_to_unfav
        testFailure(expectedMessage, true)
    }


    @ExperimentalCoroutinesApi
    @Test
    fun `Should return Failure when adding to favorites throws exception`() = runBlockingTest {
        val expectedMessage = R.string.failed_to_fav
        testFailure(expectedMessage, false)
    }

    @ExperimentalCoroutinesApi
    private fun testFailure(expectedMessage: Int, isFavorite: Boolean) = runBlockingTest {
        val movie = mock(MovieEntity::class.java)
        val exception = RuntimeException()
        mockitoWhen(movie.isFavorite).thenReturn(isFavorite)
        mockitoWhen(persistenceRepository.toggleFavoriteMovie(movie))
            .thenThrow(exception)
        toggleFavoriteMovie.execute(movie)
            .test(this)
            .assertValues(Result.Failure<Unit>(expectedMessage, exception))
            .finish()
    }

}