package com.dmytrod.cinemalist.domain

import com.dmytrod.cinemalist.data.repository.ResponseHandler
import com.dmytrod.cinemalist.domain.interactor.GetOngoingMovies
import org.koin.dsl.module

val domainModule = module {
    single { GetOngoingMovies(get()) }
}