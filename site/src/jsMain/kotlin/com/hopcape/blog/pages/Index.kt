package com.hopcape.blog.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.hopcape.blog.components.CategoryNavigationItems
import com.hopcape.blog.components.OverflowSidePanel
import com.hopcape.blog.models.ApiListResponse
import com.hopcape.blog.models.Constants.LATEST_POST_LIMIT
import com.hopcape.blog.models.PostWithoutDetails
import com.hopcape.blog.sections.HeaderSection
import com.hopcape.blog.sections.MainSection
import com.hopcape.blog.sections.PostsSection
import com.hopcape.blog.sections.SponsoredSection
import com.hopcape.blog.utils.fetchLatestPosts
import com.hopcape.blog.utils.fetchMainPosts
import com.hopcape.blog.utils.fetchSponsoredPosts
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.coroutines.launch

@Page
@Composable
fun HomePage() {
    val scope = rememberCoroutineScope()
    var mainPosts by remember {
        mutableStateOf<ApiListResponse>(ApiListResponse.Idle)
    }
    val latestPosts = remember {
        mutableStateListOf<PostWithoutDetails>()
    }

    val sponsoredPosts = remember {
        mutableStateListOf<PostWithoutDetails>()
    }

    var latestPostsToSkip by remember {
        mutableStateOf(0)
    }

    var showMoreButton by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = Unit){
        fetchMainPosts().also {response ->
            mainPosts = response
        }
        fetchLatestPosts(0).also { response ->
            when(response){
                is ApiListResponse.Error -> {}
                is ApiListResponse.Idle -> {}
                is ApiListResponse.Success -> {
                    latestPosts.addAll(response.data)
                    latestPostsToSkip += LATEST_POST_LIMIT
                    showMoreButton = response.data.size >= LATEST_POST_LIMIT
                }
            }
        }
        fetchSponsoredPosts().also { response ->
            when(response){
                is ApiListResponse.Error -> {}
                is ApiListResponse.Idle -> {}
                is ApiListResponse.Success -> sponsoredPosts.addAll(response.data)
            }
        }
    }

    val breakpoint = rememberBreakpoint()
    var overFlowMenuOpened by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (overFlowMenuOpened){
            OverflowSidePanel(
                onMenuClose = {
                    overFlowMenuOpened = false
                },
                content = {
                    CategoryNavigationItems(
                        vertical = true
                    )
                }
            )
        }
        HeaderSection(
            breakpoint = breakpoint,
            onMenuOpened = {
                overFlowMenuOpened = true
            }
        )
        MainSection(
            breakpoint = breakpoint,
            response = mainPosts
        )

        PostsSection(
            posts = latestPosts,
            showMoreVisibility = showMoreButton,
            onClick = {},
            onShowMore = {
                scope.launch {
                    fetchLatestPosts(latestPostsToSkip).also { response ->
                        when(response){
                            is ApiListResponse.Error -> {}
                            is ApiListResponse.Idle -> {}
                            is ApiListResponse.Success -> {
                                latestPosts.addAll(response.data)
                                latestPostsToSkip += LATEST_POST_LIMIT
                                showMoreButton = response.data.size >= LATEST_POST_LIMIT
                            }
                        }
                    }
                }
            },
            title = "Latest Posts",
            breakpoint = breakpoint,
            useColoredCategoryChips = false
        )

        SponsoredSection(
            breakpoint = breakpoint,
            posts = sponsoredPosts,
            onClick = {}
        )

    }
}