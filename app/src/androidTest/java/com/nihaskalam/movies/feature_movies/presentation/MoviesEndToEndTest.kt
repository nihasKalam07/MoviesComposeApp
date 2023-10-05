package com.nihaskalam.movies.feature_movies.presentation

import androidx.activity.compose.setContent
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.test.filters.LargeTest
import com.google.common.truth.Truth
import com.nihaskalam.movies.TEST_PORT
import com.nihaskalam.movies.TestUtil
import com.nihaskalam.movies.core.util.TestTags
import com.nihaskalam.movies.feature_movies.data.local.MovieDao
import com.nihaskalam.movies.feature_movies.data.local.MovieDatabase
import com.nihaskalam.movies.feature_movies.data.local.entity.MovieEntity
import com.nihaskalam.movies.feature_movies.di.MovieModule
import com.nihaskalam.movies.feature_movies.presentation.movie_details.MovieDetailsScreen
import com.nihaskalam.movies.feature_movies.presentation.movie_list.MovieListScreen
import com.nihaskalam.movies.feature_movies.presentation.search.SearchScreen
import com.nihaskalam.movies.feature_movies.presentation.util.MOVIE_ID
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
@LargeTest
class MoviesEndToEndTest {

    @Inject
    lateinit var mockWebServer: MockWebServer

    @Inject
    lateinit var movieDatabase: MovieDatabase

    lateinit var movieDao: MovieDao
    lateinit var movies: List<MovieEntity>

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
        mockWebServer.start(TEST_PORT)
        movieDao = movieDatabase.dao
        TestUtil.configureSuccessResponse(
            mockWebServer
        ) { TestUtil.readStringFromFile("success_response.json") }
        movies = TestUtil.getMovieEntityList()
        runTest {
            movieDao.insertMovies(movies)
        }
        setContentForActivity()
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
                    startDestination = "${Screen.MovieListScreen.route}/{${MOVIE_LIST_ALL}}"
                ) {
                    composable("${Screen.MovieListScreen.route}/{${MOVIE_LIST_ALL}}",
                        arguments = listOf(
                            navArgument(name = MOVIE_LIST_ALL) {
                                type = NavType.StringType
                                defaultValue = MOVIE_LIST_ALL
                            }
                        )) {
                        val listType = it.arguments?.getString(MOVIE_LIST_ALL) ?: ""
                        MovieListScreen(navController, listType)
                    }

                    composable("${Screen.MovieDetailsScreen.route}/{$MOVIE_ID}",
                        arguments = listOf(
                            navArgument(name = MOVIE_ID) {
                                type = NavType.StringType
                                defaultValue = ""
                            }
                        )
                    ) {
                        val movieId = it.arguments?.getString(MOVIE_ID) ?: ""
                        MovieDetailsScreen(navController, movieId)
                    }

                    composable(Screen.SearchScreen.route) {
                        SearchScreen(navController)
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun `fetch and display all movies - toggle favourites and search`() {
        setContentForActivity()
        //checking the all movie list is not empty
        composeRule.waitUntilAtLeastOneExists(hasTestTag(TestTags.MOVIE_ITEM))
        Truth.assertThat(
            composeRule.onAllNodesWithTag(TestTags.MOVIE_ITEM).fetchSemanticsNodes()
        ).isNotEmpty()
        composeRule.onAllNodesWithTag(TestTags.MOVIE_ITEM)[0].performClick()

        // check the details screen
        composeRule.onAllNodesWithText(movies.first().title).assertCountEquals(2)
        composeRule.onNodeWithText("Director: ${movies.first().director}").assertIsDisplayed()

        //make a movie favourite and check the same in favourite tab
        composeRule.onNodeWithContentDescription("Favorite").performClick()
        composeRule.onNodeWithContentDescription("Go back").performClick()
        composeRule.onNodeWithText("Favourites").performClick()
        composeRule.onNodeWithText("Avatar").assertIsDisplayed()

        //make a movie non-favourite and check the same is not displayed in the favourite tab
        composeRule.onAllNodesWithTag(TestTags.MOVIE_ITEM)[0].performClick()
        composeRule.onNodeWithContentDescription("Favorite").performClick()
        composeRule.onNodeWithContentDescription("Go back").performClick()
        composeRule.onNodeWithText("No Favourites").assertIsDisplayed()

        //check search results with valid and empty queries
        composeRule.onNodeWithContentDescription("Search Movies").performClick()
        //valid query provides non-empty movie list
        composeRule.onNodeWithTag(TestTags.SEARCH_TEXT_FIELD).performTextInput("avatar")
        composeRule.waitUntilAtLeastOneExists(hasTestTag(TestTags.MOVIE_ITEM))
        Truth.assertThat(composeRule.onAllNodesWithTag(TestTags.MOVIE_ITEM).fetchSemanticsNodes())
            .isNotEmpty()
        //empty query provides empty movie list
        composeRule.onNodeWithTag(TestTags.SEARCH_TEXT_FIELD).performTextClearance()
        composeRule.waitUntilAtLeastOneExists(hasText("No results found"))
        composeRule.onNodeWithText("Search movies").assertIsDisplayed()
        composeRule.onNodeWithText("No results found").assertIsDisplayed()
    }
}