package com.nihaskalam.movies.feature_movies.di

import android.content.Context
import androidx.room.Room
import com.nihaskalam.movies.BuildConfig
import com.nihaskalam.movies.REQUEST_TIMEOUT
import com.nihaskalam.movies.TEST_BASE_URL
import com.nihaskalam.movies.feature_movies.data.local.MovieDatabase
import com.nihaskalam.movies.feature_movies.data.remote.MoviesApi
import com.nihaskalam.movies.feature_movies.data.repository.MovieRepositoryImpl
import com.nihaskalam.movies.feature_movies.domain.repository.MovieRepository
import com.nihaskalam.movies.feature_movies.domain.use_case.GetAllFavourites
import com.nihaskalam.movies.feature_movies.domain.use_case.GetAllMovies
import com.nihaskalam.movies.feature_movies.domain.use_case.GetMovie
import com.nihaskalam.movies.feature_movies.domain.use_case.GetMoviesByTitle
import com.nihaskalam.movies.feature_movies.domain.use_case.MoviesUseCases
import com.nihaskalam.movies.feature_movies.domain.use_case.UpdateMovie
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestMovieModule {

    @Provides
    @Singleton
    fun provideMoviesUseCases(repository: MovieRepository): MoviesUseCases {
        return MoviesUseCases(
            getAllMovies = GetAllMovies(repository),
            getAllFavourites = GetAllFavourites(repository),
            getMoviesByTitle = GetMoviesByTitle(repository),
            updateMovie = UpdateMovie(repository),
            getMovie = GetMovie(repository)
        )
    }

    @Provides
    @Singleton
    fun provideMovieRepository(
        db: MovieDatabase,
        api: MoviesApi
    ): MovieRepository {
        return MovieRepositoryImpl(api, db.dao)
    }

    @Provides
    @Singleton
    fun provideMovieDatabase(@ApplicationContext context: Context): MovieDatabase {
        return Room.inMemoryDatabaseBuilder(context, MovieDatabase::class.java)
            .allowMainThreadQueries().build()
    }

    @Provides
    @Singleton
    fun provideHTTPLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return interceptor
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor) = if (BuildConfig.DEBUG) {
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .build()
    } else {
        OkHttpClient
            .Builder()
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(TEST_BASE_URL)
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideMoviesApi(retrofit: Retrofit) = retrofit.create(MoviesApi::class.java)

    @Provides
    @Singleton
    fun provideMockWebServer() = MockWebServer()
}