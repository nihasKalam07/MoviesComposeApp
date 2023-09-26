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
class GetMovieTest {

    private lateinit var getMovie: GetMovie
    private lateinit var fakeMovieRepository: MovieRepository

    @Before
    fun setUp() {
        fakeMovieRepository = FakeMovieRepository()
        getMovie = GetMovie(fakeMovieRepository)
        TestUtil.getMovieList().forEach {
            (fakeMovieRepository as FakeMovieRepository).insertMovie(it)
        }
    }

    @Test
    fun `get movie by imdbId returns success`() = runTest {
        val imdbID = TestUtil.getMovieList().first().imdbID
        getMovie(imdbID).test {
            val success = awaitItem()
            assertThat(success is Resource.Success && success.data?.imdbID == imdbID).isTrue()
            awaitComplete()
        }
    }
}