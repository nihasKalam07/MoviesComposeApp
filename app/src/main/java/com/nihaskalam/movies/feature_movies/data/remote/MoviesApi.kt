package com.nihaskalam.movies.feature_movies.data.remote

import com.nihaskalam.movies.feature_movies.data.remote.dto.MoviesDto
import retrofit2.http.GET

/**
 * Retrofit Api Service
 */

const val GET_MOVIES_URL = "data/api/movies.json"

interface MoviesApi {

    @GET(GET_MOVIES_URL)
    suspend fun getMovies(): MoviesDto

}