package com.nihaskalam.movies.feature_movies.presentation.movie_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.nihaskalam.movies.core.util.TestTags
import com.nihaskalam.movies.feature_movies.presentation.components.MovieGridItem
import com.nihaskalam.movies.feature_movies.presentation.util.BottomNavigationItem
import com.nihaskalam.movies.feature_movies.presentation.util.MOVIE_LIST_ALL
import com.nihaskalam.movies.feature_movies.presentation.util.MOVIE_LIST_FAVOURITES
import com.nihaskalam.movies.feature_movies.presentation.util.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListScreen(
    navController: NavHostController,
    listType: String,
    viewModel: MovieListViewModel = hiltViewModel()
) {
    if (listType == MOVIE_LIST_FAVOURITES) {
        viewModel.onEvent(MovieListEvent.ShowAllFavourites)
    } else {
        viewModel.onEvent(MovieListEvent.ShowAllMovies)
    }
    val movieListState by viewModel.movieListState.collectAsState()
    val favouritesState by viewModel.favouritesState.collectAsState()
    val items = listOf(
        BottomNavigationItem(
            title = "Movies",
            selectedIcon = Icons.Filled.Movie,
            unselectedIcon = Icons.Outlined.Movie,
        ),
        BottomNavigationItem(
            title = "Favourites",
            selectedIcon = Icons.Filled.Favorite,
            unselectedIcon = Icons.Outlined.Favorite,
        )
    )
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Movies",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    actions = {
                        IconButton(onClick = { navController.navigate(Screen.SearchScreen.route) }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search Movies"
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior,
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f)
                ) {
                    items.forEachIndexed { index, item ->
//                        println("============in Ui ${selectedTabIndexState}")
                        NavigationBarItem(
                            selected = if (listType == MOVIE_LIST_FAVOURITES) {
                                index == 1
                            } else {
                                index == 0
                            },
                            onClick = {
                                selectedItemIndex = index
                                if (selectedItemIndex == 0) {
                                    navController.navigate("${Screen.MovieListScreen.route}/${MOVIE_LIST_ALL}") {
                                        popUpTo("${Screen.MovieListScreen.route}/${MOVIE_LIST_ALL}")
                                        launchSingleTop = true
                                    }
                                } else {
                                    navController.navigate("${Screen.MovieListScreen.route}/${MOVIE_LIST_FAVOURITES}") {
                                        popUpTo("${Screen.MovieListScreen.route}/${MOVIE_LIST_FAVOURITES}")
                                        launchSingleTop = true
                                    }
                                }
//                                viewModel.onEvent(MovieListEvent.UpdateSelectedTabIndex(index))
                            },
                            label = {
                                Text(text = item.title)
                            },
                            alwaysShowLabel = true,
                            icon = {
                                BadgedBox(
                                    badge = {
                                        if (item.badgeCount != null) {
                                            Badge {
                                                Text(text = item.badgeCount.toString())
                                            }
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (index == selectedItemIndex) {
                                            item.selectedIcon
                                        } else item.unselectedIcon,
                                        contentDescription = item.title
                                    )
                                }
                            }
                        )
                    }
                }
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                LazyVerticalGrid(
                    contentPadding = PaddingValues(
                        start = 8.dp,
                        top = padding.calculateTopPadding(),
                        end = 8.dp,
                        bottom = padding.calculateBottomPadding()
                    ),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    columns = GridCells.Fixed(2), // 3 columns
                ) {
                    if (listType == MOVIE_LIST_FAVOURITES) {
                        items(
                            count = favouritesState.favourites.size,
                            key = {
                                favouritesState.favourites[it].id
                            }
                        ) { i ->
                            MovieGridItem(movie = favouritesState.favourites[i]) {
                                navController.navigate("${Screen.MovieDetailsScreen.route}/${it}")
                            }
                        }
                    } else {
                        items(
                            movieListState.movies.size,
                            key = {
                                movieListState.movies[it].id
                            }
                        ) { i ->
                            MovieGridItem(movie = movieListState.movies[i]) {
                                navController.navigate("${Screen.MovieDetailsScreen.route}/${it}")
                            }
                        }
                    }

                }
                if (movieListState.isLoading || favouritesState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .testTag(
                                TestTags.PROGRESS_INDICATOR
                            )
                    )
                }

                if (listType == MOVIE_LIST_FAVOURITES && favouritesState.favourites.isEmpty()) {
                    Text(
                        text = "No Favourites",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun MovieListScreenPreview() {
    MovieListScreen(rememberNavController(), "", hiltViewModel())
}