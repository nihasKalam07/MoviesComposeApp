package com.nihaskalam.movies.feature_movies.data.local

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.nihaskalam.movies.TEST_DB_NAME
import com.nihaskalam.movies.feature_movies.data.local.entity.MovieEntity
import com.nihaskalam.movies.feature_movies.domain.model.Movie
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import javax.inject.Named

@RunWith(AndroidJUnit4::class)
@MediumTest
@HiltAndroidTest
class MovieDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named(TEST_DB_NAME)
    lateinit var movieDatabase: MovieDatabase

    lateinit var movieDao: MovieDao

    @Before
    fun setUp() {
        hiltRule.inject()
        movieDao = movieDatabase.dao
    }

    @After
    fun tearDown() {
        movieDatabase.close()
    }

    @Test
    fun `insert movies`() = runBlocking {
        //Given
        val movies = getMovieEntityList()
        movieDao.insertMovies(movies)
        //When
        val result = movieDao.getAllMovies()
        //Then
        assertThat(result.isNullOrEmpty()).isFalse()
    }

    @Test
    fun `get all movies`() = runBlocking {
        //Given
        val movies = getMovieEntityList()
        movieDao.insertMovies(movies)
        //When
        val result = movieDao.getAllMovies()
        //Then
        assertThat(result).hasSize(movies.size)
        result.forEachIndexed { i, movie ->
            assertThat(movie.imdbID == movies[i].imdbID)
        }
    }

    @Test
    fun `get all favourites`() = runBlocking {
        //Given
        val favs = getFavouritesList()
        movieDao.insertMovies(favs)
        //When
        val result = movieDao.getAllFavourites()
        //Then
        assertThat(result).hasSize(favs.count { it.isFavourite })
    }

    @Test
    fun `get movie by id`() = runBlocking {
        //Given
        val movies = getMovieEntityList()
        movieDao.insertMovies(movies)
        //When
        val result = movieDao.getMovieById(movies.last().imdbID)
        //Then
        assertThat(result.imdbID == movies.last().imdbID).isTrue()
    }

    @Test
    fun `get movie by title`() = runBlocking {
        //Given
        val movies = getMovieEntityList()
        movieDao.insertMovies(movies)
        //When
        val result = movieDao.getMoviesByTitle(movies.first().title)
        //Then
        result.forEach {
            assertThat(it.title.contains(movies.first().title))
        }
    }

    @Test
    fun `update movie`() = runBlocking {
        //Given
        val movies = getMovieEntityList()
        movieDao.insertMovies(movies)
        //When
        assertThat(movies.first().isFavourite).isFalse()
        movieDao.update(isFavourite = true, movies.first().imdbID)
        val result = movieDao.getMovieById(movies.first().imdbID)
        //Then
        assertThat(result.isFavourite).isTrue()
    }

    @Test
    fun `delete movies`() = runBlocking {
        //Given
        val movies = getMovieEntityList()
        movieDao.insertMovies(movies)
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

    private fun getMovieEntityList(): List<MovieEntity> {
        val movie1 = Movie(title = "avatar", imdbID = "1", director = "James Cameron")
        val movie2 = Movie(title = "Inception", imdbID = "2", director = "Nolan")
        val movie3 = Movie(title = "Pulp fiction", imdbID = "3", director = "Quentin Tarantino")
        return listOf(movie1, movie2, movie3).map { it.toMovieEntity() }
    }

    private fun getFavouritesList(): List<MovieEntity> {
        val movie1 = Movie(title = "avatar", imdbID = "1", director = "James Cameron")
        val movie2 =
            Movie(title = "Inception", imdbID = "2", director = "Nolan", isFavourite = true)
        val movie3 = Movie(
            title = "Pulp fiction",
            imdbID = "3",
            director = "Quentin Tarantino",
            isFavourite = true
        )
        return listOf(movie1, movie2, movie3).map { it.toMovieEntity() }
    }
}