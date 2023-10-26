package com.hopcape.androidapp.presentation.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import com.hopcape.androidapp.RequestState
import com.hopcape.androidapp.data.Category
import com.hopcape.androidapp.data.Post
import com.hopcape.androidapp.presentation.components.PostCardsView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    posts: RequestState<List<Post>>,
    category: Category,
    onBackPressed: () -> Unit,
    onPostClick: (String?) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = category.name)
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onBackPressed()
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Menu")
                    }
                }
            )
        }
    ) {
        PostCardsView(
            posts = posts,
            onPostClick = onPostClick,
            topMargin = it.calculateTopPadding()
        )
    }
}