package com.dmytrod.cinemalist.data

import android.content.Context
import androidx.room.Room
import com.dmytrod.cinemalist.BuildConfig
import com.dmytrod.cinemalist.data.db.MovieDatabase
import com.dmytrod.cinemalist.data.remote.TMDBApiService
import com.dmytrod.cinemalist.data.repository.PersistenceRepository
import com.dmytrod.cinemalist.data.repository.RemoteRepository
import com.dmytrod.cinemalist.data.repository.ResponseHandler
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single { ResponseHandler() }
    single { RemoteRepository(get(), get()) }
    single { createTMDBApiService() }
    single { movieDatabase(androidContext()) }
    single { PersistenceRepository(get()) }
}


private fun createTMDBApiService(): TMDBApiService = Retrofit.Builder()
    .baseUrl("https://api.themoviedb.org/3/")
    .addConverterFactory(GsonConverterFactory.create())
    .client(createClient())
    .build()
    .create(TMDBApiService::class.java)

fun createClient(): OkHttpClient = OkHttpClient.Builder()
    .apply {
        if (BuildConfig.DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            addInterceptor(httpLoggingInterceptor)
        }
    }
    .build()


fun movieDatabase(appContext: Context) =
    Room.databaseBuilder(appContext, MovieDatabase::class.java, MovieDatabase.DB_FILE_NAME)
        .fallbackToDestructiveMigration()
        .build()

