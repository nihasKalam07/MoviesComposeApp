package com.nihaskalam.movies.di

import android.content.Context
import androidx.room.Room
import com.nihaskalam.movies.feature_movies.data.local.MovieDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestMovieModule {
    @Provides
    @Singleton
    @Named(TEST_DB_NAME)
    fun provideMovieDatabase(@ApplicationContext context: Context): MovieDatabase {
        return Room.inMemoryDatabaseBuilder(context, MovieDatabase::class.java)
            .allowMainThreadQueries().build()
    }

}