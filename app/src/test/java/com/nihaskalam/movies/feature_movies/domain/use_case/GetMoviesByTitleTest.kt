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
class GetMoviesByTitleTest {

    private lateinit var getMoviesByTitle: GetMoviesByTitle
    private lateinit var fakeMovieRepository: MovieRepository

    @Before
    fun setUp() {
        fakeMovieRepository = FakeMovieRepository()
        getMoviesByTitle = GetMoviesByTitle(fakeMovieRepository)
        getMovieList().forEach {
            (fakeMovieRepository as FakeMovieRepository).insertMovie(it)
        }
    }

    @Test
    fun `get movies by title returns non-empty list`() = runBlocking {
        val query = getMovieList().first().title.substring(2)
        getMoviesByTitle(query).test {
            val success = awaitItem()
            assertThat(success is Resource.Success && success.data?.isNotEmpty() == true).isTrue()
            success.data?.forEach {
                assertThat(it.title.contains(query)).isTrue()
            }
            awaitComplete()
        }
    }

    @Test
    fun `get movies by title returns empty list`() = runBlocking {
        val query = ""
        getMoviesByTitle(query).test {
            val success = awaitItem()
            assertThat(success is Resource.Success && success.data?.isEmpty() == true).isTrue()
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