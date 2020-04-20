package com.dmytrod.cinemalist.data

import com.dmytrod.cinemalist.BuildConfig
import com.dmytrod.cinemalist.data.remote.TMDBApiService
import com.dmytrod.cinemalist.data.repository.RemoteRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single { RemoteRepository(get()) }
    single {
        createTMDBApiService()
    }
}

private fun createTMDBApiService(): TMDBApiService {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(createClient())
        .build()
    return retrofit.create(TMDBApiService::class.java)
}

fun createClient(): OkHttpClient {
    val clientBuilder = OkHttpClient.Builder()
    if (BuildConfig.DEBUG) {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        clientBuilder.addInterceptor(httpLoggingInterceptor)
    }
    return clientBuilder.build()
}

