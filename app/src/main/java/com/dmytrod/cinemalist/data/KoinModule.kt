package com.dmytrod.cinemalist.data

import android.content.Context
import androidx.room.Room
import com.dmytrod.cinemalist.BuildConfig
import com.dmytrod.cinemalist.data.db.MovieDatabase
import com.dmytrod.cinemalist.data.remote.TMDBApiService
import com.dmytrod.cinemalist.data.repository.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val API_KEY = "989dd0a72eef8a89fb30f6027eb83fc7"
val dataModule = module {
    single<IResponseHandler> { ResponseHandler() }
    single<IRemoteRepository> { RemoteRepository(get(), API_KEY, get()) }
    single { createTMDBApiService() }
    single { movieDatabase(androidContext()) }
    single<IPersistenceRepository> { PersistenceRepository(get()) }
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

