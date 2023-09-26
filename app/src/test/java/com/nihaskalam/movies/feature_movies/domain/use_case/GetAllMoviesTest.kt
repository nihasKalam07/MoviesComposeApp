package com.nihaskalam.movies.feature_movies.domain.use_case

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.nihaskalam.movies.TestUtil
import com.nihaskalam.movies.core.util.Resource
import com.nihaskalam.movies.feature_movies.data.repository.FakeMovieRepository
import com.nihaskalam.movies.feature_movies.domain.repository.MovieRepository
import kotlinx.coroutines.test.runTest
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
        TestUtil.getMovieList().forEach {
            (fakeMovieRepository as FakeMovieRepository).insertMovie(it)
        }
    }

    @Test
    fun `get all movies returns success`() = runTest {
        val movieList = TestUtil.getMovieList()
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
    fun `get all movies returns api error with cached movie list`() = runTest {
        val movieList = TestUtil.getMovieList()
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

    @Test
    fun `get all movies returns api error with empty movie list`() = runTest {
        val movieList = TestUtil.getMovieList()
        val repository = fakeMovieRepository as FakeMovieRepository
        repository.deleteMovies()
        repository.setShouldReturnNetworkError(true)
        getAllMovies().test {
            val loadingWithoutData = awaitItem()
            assertThat(loadingWithoutData is Resource.Loading && loadingWithoutData.data == null).isTrue()
            val loadingWithData = awaitItem()
            assertThat(loadingWithData is Resource.Loading && loadingWithData.data?.isEmpty() == true).isTrue()
            val apiError = awaitItem()
            assertThat(
                apiError is Resource.Error &&
                        apiError.data?.isEmpty() == true &&
                        apiError.message == FakeMovieRepository.API_ERROR_MESSAGE
            ).isTrue()
            awaitComplete()
        }
    }
}