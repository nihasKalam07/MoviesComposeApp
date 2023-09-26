package com.nihaskalam.movies.feature_movies.presentation.search

import com.google.common.truth.Truth
import com.nihaskalam.movies.MainCoroutineRule
import com.nihaskalam.movies.TestUtil
import com.nihaskalam.movies.feature_movies.data.repository.FakeMovieRepository
import com.nihaskalam.movies.feature_movies.domain.repository.MovieRepository
import com.nihaskalam.movies.feature_movies.domain.use_case.GetAllFavourites
import com.nihaskalam.movies.feature_movies.domain.use_case.GetAllMovies
import com.nihaskalam.movies.feature_movies.domain.use_case.GetMovie
import com.nihaskalam.movies.feature_movies.domain.use_case.GetMoviesByTitle
import com.nihaskalam.movies.feature_movies.domain.use_case.MoviesUseCases
import com.nihaskalam.movies.feature_movies.domain.use_case.UpdateMovie
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: SearchViewModel
    private lateinit var moviesUseCases: MoviesUseCases
    private lateinit var fakeRepository: MovieRepository

    @Before
    fun setUp() {
        fakeRepository = FakeMovieRepository()
        moviesUseCases = MoviesUseCases(
            getAllMovies = GetAllMovies(fakeRepository),
            getAllFavourites = GetAllFavourites(fakeRepository),
            getMoviesByTitle = GetMoviesByTitle(fakeRepository),
            updateMovie = UpdateMovie(fakeRepository),
            getMovie = GetMovie(fakeRepository)
        )
        viewModel = SearchViewModel(moviesUseCases)
        TestUtil.getMovieList().forEach {
            (fakeRepository as FakeMovieRepository).insertMovie(it)
        }
    }

    @Test
    fun `search movies by query, returns movie list`() = runTest {
        val query = TestUtil.getMovieList().first().title.substring(2)
        viewModel.onEvent(SearchEvent.SearchMovie(query))
        viewModel.searchItemsState.value.let { searchState ->
            Truth.assertThat(!searchState.isLoading).isTrue()
            searchState.movies.forEach {
                Truth.assertThat(it.title.contains(query)).isTrue()
            }
        }
    }

    @Test
    fun `search movies by empty query, returns empty list`() = runTest {
        val query = ""
        viewModel.onEvent(SearchEvent.SearchMovie(query))
        viewModel.searchItemsState.value.let {
            Truth.assertThat(!it.isLoading && it.movies.isEmpty()).isTrue()
        }
    }
}