package com.hopcape.androidapp.presentation.screens

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import com.hopcape.androidapp.RequestState
import com.hopcape.androidapp.data.Category
import com.hopcape.androidapp.data.Post
import com.hopcape.androidapp.presentation.components.NavigationDrawer
import com.hopcape.androidapp.presentation.components.PostCardsView
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    posts: RequestState<List<Post>>,
    searchedPosts: RequestState<List<Post>>,
    searchBarOpened: Boolean = false,
    active: Boolean = false,
    query: String = "",
    onQueryChange: (String) -> Unit = {},
    onSearch: (String) -> Unit = {},
    onActiveChange: (Boolean) -> Unit = {},
    onSearchBarChange: (Boolean) -> Unit = {},
    onCategorySelected: (Category) -> Unit = {},
    onPostClick: (String?) -> Unit

) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    NavigationDrawer(
        drawerState = drawerState,
        onCategorySelected = {
            scope.launch {
                drawerState.close()
            }
            onCategorySelected(it)
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(text = "Android Blog")
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            onSearchBarChange(true)
                            onActiveChange(true)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Menu",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                )
                if (searchBarOpened) {
                    SearchBar(
                        query = query,
                        onQueryChange = onQueryChange,
                        onSearch = onSearch,
                        active = active,
                        onActiveChange = onActiveChange,
                        leadingIcon = {
                            IconButton(onClick = { onSearchBarChange(false) }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Menu",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        },
                        trailingIcon = {
                            IconButton(onClick = { onQueryChange("") }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Menu",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    ) {
                        PostCardsView(
                            posts = searchedPosts,
                            onPostClick = onPostClick,
                            topMargin = 12.dp
                        )
                    }
                }
            }
        ) {
            PostCardsView(
                posts = posts,
                onPostClick = onPostClick,
                topMargin = it.calculateTopPadding()
            )
        }
    }
}