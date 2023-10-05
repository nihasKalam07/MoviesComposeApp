package com.nihaskalam.movies.feature_movies.presentation.movie_list

import androidx.activity.compose.setContent
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.common.truth.Truth.assertThat
import com.nihaskalam.movies.TEST_PORT
import com.nihaskalam.movies.TestUtil
import com.nihaskalam.movies.TestUtil.configureSuccessResponse
import com.nihaskalam.movies.core.util.TestTags
import com.nihaskalam.movies.feature_movies.data.local.MovieDao
import com.nihaskalam.movies.feature_movies.data.local.MovieDatabase
import com.nihaskalam.movies.feature_movies.di.MovieModule
import com.nihaskalam.movies.feature_movies.presentation.MainActivity
import com.nihaskalam.movies.feature_movies.presentation.util.MOVIE_LIST_ALL
import com.nihaskalam.movies.feature_movies.presentation.util.Screen
import com.nihaskalam.movies.ui.theme.MoviesTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@UninstallModules(MovieModule::class)
class MovieListScreenTest {

    @Inject
    lateinit var mockWebServer: MockWebServer

    @Inject
    lateinit var movieDatabase: MovieDatabase

    lateinit var movieDao: MovieDao

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
        mockWebServer.start(TEST_PORT)
        movieDao = movieDatabase.dao
        configureSuccessResponse(
            mockWebServer
        ) { TestUtil.readStringFromFile("success_response.json") }
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        movieDatabase.close()
    }

    private fun setContentForActivity(listType: String = MOVIE_LIST_ALL) {
        composeRule.activity.setContent {
            MoviesTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "${Screen.MovieListScreen.route}/{$listType}"
                ) {
                    composable("${Screen.MovieListScreen.route}/{$listType}",
                        arguments = listOf(
                            navArgument(name = listType) {
                                type = NavType.StringType
                                defaultValue = listType
                            }
                        )) {
                        val listType = it.arguments?.getString(listType) ?: ""
                        MovieListScreen(navController, listType)
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun `all movie list from api is not empty`() {
        setContentForActivity()
        composeRule.waitUntilAtLeastOneExists(hasTestTag(TestTags.MOVIE_ITEM))
        assertThat(
            composeRule.onAllNodesWithTag(TestTags.MOVIE_ITEM).fetchSemanticsNodes()
        ).isNotEmpty()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun `all movie list from both api and database are empty`() {
        configureSuccessResponse(
            mockWebServer
        ) { "" }
        setContentForActivity()
        composeRule.waitUntilDoesNotExist(hasTestTag(TestTags.PROGRESS_INDICATOR))
        assertThat(composeRule.onAllNodesWithTag(TestTags.MOVIE_ITEM).fetchSemanticsNodes()).isEmpty()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun `all movie list from api is empty but getting from database`() {
        configureSuccessResponse(
            mockWebServer
        ) { "" }
        val movies = TestUtil.getMovieEntityList()
        runTest {
            movieDao.insertMovies(movies)
        }
        setContentForActivity()
        composeRule.waitUntilAtLeastOneExists(hasTestTag(TestTags.MOVIE_ITEM))
        assertThat(composeRule.onAllNodesWithTag(TestTags.MOVIE_ITEM).fetchSemanticsNodes()).isNotEmpty()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun `favourite movie list is not empty`() {
        val movies = TestUtil.getFavouritesList()
        runTest {
            movieDao.insertMovies(movies)
        }
        setContentForActivity()
        composeRule.onNodeWithText("Favourites").performClick()
        composeRule.waitUntilAtLeastOneExists(hasTestTag(TestTags.MOVIE_ITEM))
        assertThat(composeRule.onAllNodesWithTag(TestTags.MOVIE_ITEM).fetchSemanticsNodes()).hasSize(3)
    }
}