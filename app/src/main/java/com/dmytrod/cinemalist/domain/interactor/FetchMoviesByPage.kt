package com.dmytrod.cinemalist.domain.interactor

import com.dmytrod.cinemalist.R
import com.dmytrod.cinemalist.data.repository.*
import kotlinx.coroutines.flow.flow

/**
 * Interactor for loading ongoing movies from TMDB api and saving results in DB
 */
class FetchMoviesByPage(
    private val remoteRepository: IRemoteRepository,
    private val persistenceRepository: IPersistenceRepository
) : FlowInteractor<Int, PageData> {

    /**
     * @param param page number
     */
    override fun execute(param: Int) = flow {
        val result = when (val response = remoteRepository.getOngoingMovies(param)) {
            is IResponseHandler.Response.Success -> {
                val data = response.data
                try {
                    persistenceRepository.storeList(data.results)
                    Result.Success(PageData(data.totalPages, data.page))
                } catch (e: Throwable) {
                    Result.Failure<PageData>(R.string.failed_to_store_to_db, e)
                }
            }
            is IResponseHandler.Response.Error -> Result.Failure<PageData>(
                response.remoteError.errorMessageRes,
                response.remoteError.cause
            )
        }
        emit(result)
    }

}