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
class GetMoviesByTitleTest {

    private lateinit var getMoviesByTitle: GetMoviesByTitle
    private lateinit var fakeMovieRepository: MovieRepository

    @Before
    fun setUp() {
        fakeMovieRepository = FakeMovieRepository()
        getMoviesByTitle = GetMoviesByTitle(fakeMovieRepository)
        TestUtil.getMovieList().forEach {
            (fakeMovieRepository as FakeMovieRepository).insertMovie(it)
        }
    }

    @Test
    fun `get movies by title returns non-empty list`() = runTest {
        val query = TestUtil.getMovieList().first().title.substring(2)
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
    fun `get movies by title where query string is empty, returns empty list`() = runTest {
        val query = ""
        getMoviesByTitle(query).test {
            val success = awaitItem()
            assertThat(success is Resource.Success && success.data?.isEmpty() == true).isTrue()
            awaitComplete()
        }
    }
}