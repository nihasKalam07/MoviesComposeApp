package com.nihaskalam.movies

import androidx.test.platform.app.InstrumentationRegistry
import com.nihaskalam.movies.feature_movies.data.local.entity.MovieEntity
import com.nihaskalam.movies.feature_movies.domain.model.Movie
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import java.io.IOException
import java.io.InputStreamReader

object TestUtil {
    fun getMovieEntityList(): List<MovieEntity> {
        val movie1 = Movie(title = "Avatar", imdbID = "1", director = "James Cameron")
        val movie2 = Movie(title = "Inception", imdbID = "2", director = "Christopher Nolan")
        val movie3 = Movie(title = "Pulp fiction", imdbID = "3", director = "Quentin Tarantino")
        return listOf(movie1, movie2, movie3).map { it.toMovieEntity() }
    }

    fun getFavouritesList(): List<MovieEntity> {
        val movie1 = Movie(title = "Avatar", imdbID = "1", director = "James Cameron", isFavourite = true)
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

    /**
     * Method to set up success response for mock webserver
     *
     * @param mockWebServer MockWebServer
     */
    fun configureSuccessResponse(mockWebServer: MockWebServer, getResponse: () -> String) {
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse()
                    .setResponseCode(200)
                    .setBody(getResponse())
            }
        }
    }

    /**
     * Function to read files from asset
     *
     * @param fileName File name
     * @return file as String
     */
    fun readStringFromFile(fileName: String): String {
        try {
            val inputStream = (InstrumentationRegistry.getInstrumentation().targetContext
                .applicationContext).assets.open(fileName)
            val builder = StringBuilder()
            val reader = InputStreamReader(inputStream, "UTF-8")
            reader.readLines().forEach {
                builder.append(it)
            }
            return builder.toString()
        } catch (e: IOException) {
            throw e
        }
    }
}
