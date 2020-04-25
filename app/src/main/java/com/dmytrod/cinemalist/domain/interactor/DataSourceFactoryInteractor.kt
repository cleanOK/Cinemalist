package com.dmytrod.cinemalist.domain.interactor

import androidx.paging.DataSource

interface DataSourceFactoryInteractor<T> {
    fun execute(): DataSource.Factory<Int, T>
}