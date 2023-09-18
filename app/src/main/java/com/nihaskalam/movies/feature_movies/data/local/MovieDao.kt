package com.nihaskalam.movies.feature_movies.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nihaskalam.movies.feature_movies.data.local.entity.MovieEntity

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Query("SELECT * FROM movieEntity")
    suspend fun getAllMovies(): List<MovieEntity>

    @Query("SELECT * FROM movieEntity WHERE isFavourite = 1")
    suspend fun getAllFavourites(): List<MovieEntity>

    @Query("DELETE FROM movieEntity WHERE imdbID IN(:imdbIDs)")
    suspend fun deleteMovies(imdbIDs: List<String>)

    @Query("SELECT * FROM movieEntity WHERE imdbID LIKE :imdbID LIMIT 1")
    suspend fun getMovieById(imdbID: String): MovieEntity

    @Query("UPDATE movieEntity SET isFavourite=:isFavourite WHERE imdbID = :imdbID")
    suspend fun update(isFavourite: Boolean, imdbID: String)

    @Query("SELECT * FROM movieEntity WHERE title LIKE '%' || :title || '%'")
    suspend fun getMoviesByTitle(title: String): List<MovieEntity>

}