package com.nihaskalam.movies.feature_movies.di

import android.app.Application
import androidx.room.Room
import com.nihaskalam.movies.BuildConfig
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
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MovieModule {

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
    fun provideMovieDatabase(app: Application): MovieDatabase {
        return Room.databaseBuilder(
            app, MovieDatabase::class.java, MovieDatabase.DB_NAME
        ).build()
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
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideMoviesApi(retrofit: Retrofit) = retrofit.create(MoviesApi::class.java)

}