package com.nihaskalam.movies.feature_movies.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nihaskalam.movies.feature_movies.data.local.entity.MovieEntity

@Database(
    entities = [MovieEntity::class],
    version = 1
)
abstract class MovieDatabase: RoomDatabase() {

    abstract val dao: MovieDao

    companion object {
        const val DB_NAME = "movie_db"
    }
}