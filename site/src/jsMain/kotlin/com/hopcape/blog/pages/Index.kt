package com.hopcape.blog.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.hopcape.blog.components.CategoryNavigationItems
import com.hopcape.blog.components.OverflowSidePanel
import com.hopcape.blog.models.ApiListResponse
import com.hopcape.blog.models.ApiResponse
import com.hopcape.blog.sections.HeaderSection
import com.hopcape.blog.sections.MainSection
import com.hopcape.blog.utils.fetchMainPosts
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint

@Page
@Composable
fun HomePage() {
    val scope = rememberCoroutineScope()
    var mainPosts by remember {
        mutableStateOf<ApiListResponse>(ApiListResponse.Idle)
    }

    LaunchedEffect(key1 = Unit){
        fetchMainPosts().also {response ->
            mainPosts = response
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
    }
}