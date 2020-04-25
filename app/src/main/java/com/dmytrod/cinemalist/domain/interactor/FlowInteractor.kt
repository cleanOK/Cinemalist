package com.dmytrod.cinemalist.domain.interactor

import kotlinx.coroutines.flow.Flow

interface FlowInteractor<T, R> {
    fun execute(param: T): Flow<Result<R>>
}