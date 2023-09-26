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
class GetAllFavouritesTest {

    private lateinit var getAllFavourites: GetAllFavourites
    private lateinit var fakeMovieRepository: MovieRepository

    @Before
    fun setUp() {
        fakeMovieRepository = FakeMovieRepository()
        getAllFavourites = GetAllFavourites(fakeMovieRepository)
        getFavouritesList().forEach {
            (fakeMovieRepository as FakeMovieRepository).insertMovie(it)
        }
    }

    @Test
    fun `get all favourites returns success`() = runBlocking {
        getAllFavourites().test {
            val success = awaitItem()
            assertThat(
                success is Resource.Success &&
                        success.data?.size == 1 &&
                        success.data?.first()?.isFavourite == true
            ).isTrue()
            awaitComplete()
        }
    }

    private fun getFavouritesList(): List<Movie> {
        val movie1 =
            Movie(title = "avatar", imdbID = "1", director = "James Cameron", isFavourite = true)
        val movie2 = Movie(title = "Inception", imdbID = "2", director = "Nolan")
        val movie3 = Movie(title = "Pulp fiction", imdbID = "3", director = "Quentin Tarantino")
        return listOf(movie1, movie2, movie3)
    }
}