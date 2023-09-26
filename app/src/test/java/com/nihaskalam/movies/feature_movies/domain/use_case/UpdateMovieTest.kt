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
class UpdateMovieTest {

    private lateinit var updateMovie: UpdateMovie
    private lateinit var fakeMovieRepository: MovieRepository

    @Before
    fun setUp() {
        fakeMovieRepository = FakeMovieRepository()
        updateMovie = UpdateMovie(fakeMovieRepository)
        TestUtil.getMovieList().forEach {
            (fakeMovieRepository as FakeMovieRepository).insertMovie(it)
        }
    }

    @Test
    fun `update movie with isFavourite true, returns success`() = runTest {
        val isFav = true
        val imdbID = TestUtil.getMovieList().first().imdbID
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
}