package com.nihaskalam.movies.feature_movies.domain.use_case

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.nihaskalam.movies.core.util.Resource
import com.nihaskalam.movies.feature_movies.data.repository.FakeMovieRepository
import com.nihaskalam.movies.feature_movies.domain.model.Movie
import com.nihaskalam.movies.feature_movies.domain.repository.MovieRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class UpdateMovieTest {

    private lateinit var updateMovie: UpdateMovie
    private lateinit var fakeMovieRepository: MovieRepository

    @Before
    fun setUp() {
        fakeMovieRepository = FakeMovieRepository()
        updateMovie = UpdateMovie(fakeMovieRepository)
        getMovieList().forEach {
            (fakeMovieRepository as FakeMovieRepository).insertMovie(it)
        }
    }

    @Test
    fun `update movie with isFavourite true, returns success`() = runBlocking {
        val isFav = true
        val imdbID = getMovieList().first().imdbID
        updateMovie(isFav, imdbID).test {
            val success = awaitItem()
            assertThat(
                success is Resource.Success &&
                        success.data?.imdbID == imdbID &&
                        success.data?.isFavourite == true
            ).isTrue()
            awaitComplete()
        }
    }


    private fun getMovieList(): List<Movie> {
        val movie1 = Movie(title = "avatar", imdbID = "1", director = "James Cameron")
        val movie2 = Movie(title = "Inception", imdbID = "2", director = "Nolan")
        val movie3 = Movie(title = "Pulp fiction", imdbID = "3", director = "Quentin Tarantino")
        return listOf(movie1, movie2, movie3)
    }
}