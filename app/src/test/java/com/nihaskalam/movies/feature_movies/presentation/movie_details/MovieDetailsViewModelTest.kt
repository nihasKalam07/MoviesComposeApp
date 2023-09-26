package com.nihaskalam.movies.feature_movies.presentation.movie_details

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

class MovieDetailsViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: MovieDetailsViewModel
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
        viewModel = MovieDetailsViewModel(moviesUseCases)
        TestUtil.getMovieList().forEach {
            (fakeRepository as FakeMovieRepository).insertMovie(it)
        }
    }

    @Test
    fun `get movie, return success`() = runTest {
        val movie = TestUtil.getMovieList().last()
        viewModel.onEvent(MovieDetailsEvent.FetchMovieDetails(movie.imdbID))
        viewModel.movieState.value.let {
            Truth.assertThat(it.movie == movie && !it.isLoading).isTrue()
        }
    }

    @Test
    fun `update movie with favourite true`() = runTest {
        val movie = TestUtil.getMovieList().last()
        viewModel.onEvent(MovieDetailsEvent.FetchMovieDetails(movie.imdbID))
        viewModel.onEvent(MovieDetailsEvent.UpdateMovie(isFavourite = true))
        viewModel.movieState.value.let {
            Truth.assertThat(it.movie?.isFavourite == true && !it.isLoading).isTrue()
        }
    }

    @Test
    fun `update movie with favourite false`() = runTest {
        val movie = TestUtil.getMovieList().last()
        viewModel.onEvent(MovieDetailsEvent.FetchMovieDetails(movie.imdbID))
        viewModel.onEvent(MovieDetailsEvent.UpdateMovie(isFavourite = false))
        viewModel.movieState.value.let {
            Truth.assertThat(it.movie?.isFavourite == false && !it.isLoading).isTrue()
        }
    }
}