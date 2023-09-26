package com.nihaskalam.movies.feature_movies.presentation.movie_list

import com.google.common.truth.Truth.assertThat
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


class MovieListViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: MovieListViewModel
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
        viewModel = MovieListViewModel(moviesUseCases)
        TestUtil.getMovieList().forEach {
            (fakeRepository as FakeMovieRepository).insertMovie(it)
        }
    }

    @Test
    fun `get all movies, return success`() = runTest {
        viewModel.onEvent(MovieListEvent.ShowAllMovies)
        viewModel.movieListState.value.let {
            assertThat(it.movies == TestUtil.getMovieList() && !it.isLoading).isTrue()
        }
    }

    @Test
    fun `get all movies, return api error with cached movie list`() = runTest {
        val repository = fakeRepository as FakeMovieRepository
        repository.setShouldReturnNetworkError(true)
        viewModel.onEvent(MovieListEvent.ShowAllMovies)
        viewModel.movieListState.value.let {
            assertThat(it.movies == TestUtil.getMovieList() && !it.isLoading).isTrue()
        }
    }

    @Test
    fun `get all movies, return api error with empty list`() = runTest {
        val repository = fakeRepository as FakeMovieRepository
        repository.deleteMovies()
        repository.setShouldReturnNetworkError(true)
        viewModel.onEvent(MovieListEvent.ShowAllMovies)
        viewModel.movieListState.value.let {
            assertThat(it.movies.isEmpty() && !it.isLoading).isTrue()
        }
    }

    @Test
    fun `get all favourites, return success with movie list`() = runTest {
        viewModel.onEvent(MovieListEvent.ShowAllFavourites)
        viewModel.favouritesState.value.let {
            assertThat(
                it.favourites.size == 1 &&
                        it.favourites.first().isFavourite &&
                        !it.isLoading
            ).isTrue()
        }
    }
}