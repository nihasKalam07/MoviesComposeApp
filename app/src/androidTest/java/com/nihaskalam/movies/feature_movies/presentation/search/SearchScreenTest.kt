package com.nihaskalam.movies.feature_movies.presentation.search

import androidx.activity.compose.setContent
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.common.truth.Truth
import com.nihaskalam.movies.TestUtil
import com.nihaskalam.movies.core.util.TestTags
import com.nihaskalam.movies.feature_movies.data.local.MovieDao
import com.nihaskalam.movies.feature_movies.data.local.MovieDatabase
import com.nihaskalam.movies.feature_movies.data.local.entity.MovieEntity
import com.nihaskalam.movies.feature_movies.di.MovieModule
import com.nihaskalam.movies.feature_movies.presentation.MainActivity
import com.nihaskalam.movies.feature_movies.presentation.movie_list.MovieListScreen
import com.nihaskalam.movies.feature_movies.presentation.util.MOVIE_LIST_ALL
import com.nihaskalam.movies.feature_movies.presentation.util.Screen
import com.nihaskalam.movies.ui.theme.MoviesTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@UninstallModules(MovieModule::class)
class SearchScreenTest {
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
        movieDao = movieDatabase.dao
        movies = TestUtil.getMovieEntityList()
        runTest {
            movieDao.insertMovies(movies)
        }
        setContentForActivity()
    }

    @After
    fun tearDown() {
        movieDatabase.close()
    }

    private fun setContentForActivity(listType: String = MOVIE_LIST_ALL) {
        composeRule.activity.setContent {
            MoviesTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "${Screen.MovieListScreen.route}/{$MOVIE_LIST_ALL}"
                ) {
                    composable("${Screen.MovieListScreen.route}/{$MOVIE_LIST_ALL}",
                        arguments = listOf(
                            navArgument(name = MOVIE_LIST_ALL) {
                                type = NavType.StringType
                                defaultValue = MOVIE_LIST_ALL
                            }
                        )) {
                        val listType = it.arguments?.getString(MOVIE_LIST_ALL) ?: ""
                        MovieListScreen(navController, listType)
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
    fun `search results are not empty for valid query`() {
        setContentForActivity()
        composeRule.waitUntilAtLeastOneExists(hasTestTag(TestTags.MOVIE_ITEM))
        composeRule.onNodeWithContentDescription("Search Movies").performClick()
        composeRule.onNodeWithTag(TestTags.SEARCH_TEXT_FIELD).performTextInput("avatar")
        composeRule.waitUntilAtLeastOneExists(hasTestTag(TestTags.MOVIE_ITEM))
        Truth.assertThat(composeRule.onAllNodesWithTag(TestTags.MOVIE_ITEM).fetchSemanticsNodes()).isNotEmpty()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun `search result is empty for empty query`() {
        setContentForActivity()
        composeRule.waitUntilAtLeastOneExists(hasTestTag(TestTags.MOVIE_ITEM))
        composeRule.onNodeWithContentDescription("Search Movies").performClick()
        composeRule.onNodeWithTag(TestTags.SEARCH_TEXT_FIELD).performTextInput("")
        composeRule.onNodeWithText("Search movies").assertIsDisplayed()
        composeRule.onNodeWithText("No results found").assertIsDisplayed()
    }
}