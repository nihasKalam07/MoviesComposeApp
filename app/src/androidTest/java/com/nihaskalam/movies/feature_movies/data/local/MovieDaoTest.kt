package com.nihaskalam.movies.feature_movies.data.local

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.nihaskalam.movies.TestUtil
import com.nihaskalam.movies.feature_movies.data.local.entity.MovieEntity
import com.nihaskalam.movies.feature_movies.di.MovieModule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@MediumTest
@HiltAndroidTest
@UninstallModules(MovieModule::class)
class MovieDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var movieDatabase: MovieDatabase

    lateinit var movieDao: MovieDao
    lateinit var movies: List<MovieEntity>

    @Before
    fun setUp() {
        hiltRule.inject()
        movieDao = movieDatabase.dao
        movies = TestUtil.getMovieEntityList()
        runTest {
            movieDao.insertMovies(movies)
        }
    }

    @After
    fun tearDown() {
        movieDatabase.close()
    }

    @Test
    fun `insert movies`() = runTest {
        //When
        val result = movieDao.getAllMovies()
        //Then
        assertThat(result.isNullOrEmpty()).isFalse()
    }

    @Test
    fun `get all movies`() = runTest {
        //When
        val result = movieDao.getAllMovies()
        //Then
        assertThat(result).hasSize(movies.size)
        result.forEachIndexed { i, movie ->
            assertThat(movie.imdbID == movies[i].imdbID)
        }
    }

    @Test
    fun `get all favourites`() = runTest {
        //Given
        val favs = TestUtil.getFavouritesList()
        movieDao.insertMovies(favs)
        //When
        val result = movieDao.getAllFavourites()
        //Then
        assertThat(result).hasSize(favs.count { it.isFavourite })
    }

    @Test
    fun `get movie by id`() = runTest {
        //When
        val result = movieDao.getMovieById(movies.last().imdbID)
        //Then
        assertThat(result.imdbID == movies.last().imdbID).isTrue()
    }

    @Test
    fun `get movie by title`() = runTest {
        //When
        val result = movieDao.getMoviesByTitle(movies.first().title)
        //Then
        result.forEach {
            assertThat(it.title.contains(movies.first().title))
        }
    }

    @Test
    fun `update movie`() = runTest {
        //When
        assertThat(movies.first().isFavourite).isFalse()
        movieDao.update(isFavourite = true, movies.first().imdbID)
        val result = movieDao.getMovieById(movies.first().imdbID)
        //Then
        assertThat(result.isFavourite).isTrue()
    }

    @Test
    fun `delete movies`() = runTest {
        //when
        val insertedMovies = movieDao.getAllMovies()
        //Then
        assertThat(insertedMovies).hasSize(movies.size)
        //When
        movieDao.deleteMovies(listOf(movies.first().imdbID))
        val remainingMovies = movieDao.getAllMovies()
        //Then
        assertThat(remainingMovies).hasSize(movies.size - 1)
    }
}