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
class GetAllMoviesTest {

    private lateinit var getAllMovies: GetAllMovies
    private lateinit var fakeMovieRepository: MovieRepository

    @Before
    fun setUp() {
        fakeMovieRepository = FakeMovieRepository()
        getAllMovies = GetAllMovies(fakeMovieRepository)
        getMovieList().forEach {
            (fakeMovieRepository as FakeMovieRepository).insertMovie(it)
        }
    }

    @Test
    fun `get all movies returns success`() = runBlocking {
        val movieList = getMovieList()
        getAllMovies().test {
            val loadingWithoutData = awaitItem()
            assertThat(loadingWithoutData is Resource.Loading && loadingWithoutData.data == null).isTrue()
            val loadingWithData = awaitItem()
            assertThat(loadingWithData is Resource.Loading && loadingWithData.data == movieList).isTrue()
            val success = awaitItem()
            assertThat(success is Resource.Success && success.data == movieList).isTrue()
            awaitComplete()
//            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `get all movies returns api error`() = runBlocking {
        val movieList = getMovieList()
        val repository = fakeMovieRepository as FakeMovieRepository
        repository.setShouldReturnNetworkError(true)
        getAllMovies().test {
            val loadingWithoutData = awaitItem()
            assertThat(loadingWithoutData is Resource.Loading && loadingWithoutData.data == null).isTrue()
            val loadingWithData = awaitItem()
            assertThat(loadingWithData is Resource.Loading && loadingWithData.data == movieList).isTrue()
            val apiError = awaitItem()
            assertThat(
                apiError is Resource.Error &&
                        apiError.data == movieList &&
                        apiError.message == FakeMovieRepository.API_ERROR_MESSAGE
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