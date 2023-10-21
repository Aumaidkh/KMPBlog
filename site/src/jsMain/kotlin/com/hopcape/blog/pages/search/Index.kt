package com.hopcape.blog.pages.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.hopcape.blog.components.CategoryNavigationItems
import com.hopcape.blog.components.LoadingIndicator
import com.hopcape.blog.components.OverflowSidePanel
import com.hopcape.blog.models.ApiListResponse
import com.hopcape.blog.models.Category
import com.hopcape.blog.models.Constants.CATEGORY_PARAM
import com.hopcape.blog.models.Constants.POST_PER_PAGE
import com.hopcape.blog.models.Constants.QUERY_PARAM
import com.hopcape.blog.models.PostWithoutDetails
import com.hopcape.blog.models.Theme
import com.hopcape.blog.navigation.Screen
import com.hopcape.blog.sections.FooterSection
import com.hopcape.blog.sections.Header
import com.hopcape.blog.sections.PostsSection
import com.hopcape.blog.utils.Constants
import com.hopcape.blog.utils.Id
import com.hopcape.blog.utils.Resource
import com.hopcape.blog.utils.searchPostsByCategory
import com.hopcape.blog.utils.searchPostsByTitle
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxHeight
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.browser.document
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.get

@Page(routeOverride = "query")
@Composable
fun SearchPage() {
    val context = rememberPageContext()
    val breakpoint = rememberBreakpoint()
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(true) }
    val hasCategoryParam by remember(context.route) {
        mutableStateOf(context.route.queryParams.containsKey(CATEGORY_PARAM))
    }
    val hasQueryParam by remember(context.route) { mutableStateOf(context.route.queryParams.containsKey(QUERY_PARAM)) }
    var skip by remember { mutableStateOf(0) }
    var showMore by remember { mutableStateOf(false) }
    var overFlowMenuOpened by remember { mutableStateOf(false) }
    val posts = remember {
        mutableStateListOf<PostWithoutDetails>()
    }

    val category = remember(context.route) {
        if (hasCategoryParam) {
            context.route.params[CATEGORY_PARAM]?.let {
                runCatching {
                    Category.valueOf(it)
                }.getOrElse {
                    Category.Programming
                }
            } ?: Category.Programming
        } else {
            Category.Programming
        }
    }


    LaunchedEffect(key1 = context.route) {
        showMore = false
        (document.getElementById(Id.searchInput) as HTMLInputElement).value = ""
        skip = 0
        if (hasCategoryParam) {
            searchPostsByCategory(
                category = category
            ).also { response ->
                when (response) {
                    is ApiListResponse.Error -> {
                        isLoading = false
                    }
                    is ApiListResponse.Idle -> {
                        isLoading = true
                    }
                    is ApiListResponse.Success -> {
                        posts.clear()
                        posts.addAll(response.data)
                        isLoading = false
                        skip += POST_PER_PAGE
                        showMore = response.data.size >= POST_PER_PAGE
                    }
                }
            }
        } else if (hasQueryParam) {
            val query = context.route.queryParams[QUERY_PARAM] ?: ""
            // Add value to the search input field as well
            (document.getElementById(Id.searchInput) as HTMLInputElement).value = query.replace("%20"," ")
            searchPostsByTitle(
                query = query,
                skip = 0
            ).also { response ->
                when (response) {
                    is ApiListResponse.Error -> {
                        isLoading = false
                    }
                    is ApiListResponse.Idle -> {
                        isLoading = true
                    }
                    is ApiListResponse.Success -> {
                        posts.clear()
                        posts.addAll(response.data)
                        isLoading = false
                        skip += POST_PER_PAGE
                        showMore = response.data.size >= POST_PER_PAGE
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .maxHeight(100.vh),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (overFlowMenuOpened) {
            OverflowSidePanel(
                onMenuClose = {
                    overFlowMenuOpened = false
                },
                content = {
                    CategoryNavigationItems(
                        vertical = true,
                        selectedCategory = if (hasCategoryParam) category else null
                    )
                }
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .backgroundColor(Theme.Secondary.rgb),
            contentAlignment = Alignment.Center
        ) {
            Header(
                breakpoint = breakpoint,
                onMenuClicked = {
                    overFlowMenuOpened = true
                },
                logo = Resource.Image.logo,
                selectedCategory = if (hasCategoryParam) category else null
            )
        }

        if (hasCategoryParam && !isLoading){
            SpanText(
                modifier = Modifier
                    .fillMaxWidth()
                    .textAlign(TextAlign.Center)
                    .margin(topBottom = 50.px)
                    .color(Colors.Black)
                    .fontFamily(Constants.FONT_FAMILY)
                    .fontWeight(FontWeight.Medium)
                    .fontSize(20.px),
                text = category.name
            )
        }

        if (!isLoading){
            PostsSection(
                title = null,
                breakpoint = breakpoint,
                posts = posts,
                showMoreVisibility = showMore,
                onShowMore = {
                    scope.launch {
                        if (hasCategoryParam) {
                            searchPostsByCategory(
                                category = category,
                                skip = skip
                            ).also { response ->
                                when (response) {
                                    is ApiListResponse.Error -> {
                                        isLoading = false
                                    }
                                    is ApiListResponse.Idle -> {
                                        isLoading = true
                                    }
                                    is ApiListResponse.Success -> {
                                        posts.addAll(response.data)
                                        isLoading = false
                                        skip += POST_PER_PAGE
                                        showMore = response.data.size >= POST_PER_PAGE
                                    }
                                }
                            }
                        } else if (hasQueryParam) {
                            if (hasQueryParam) {
                                searchPostsByTitle(
                                    query = context.route.queryParams[QUERY_PARAM] ?: "",
                                    skip = skip
                                ).also { response ->
                                    when (response) {
                                        is ApiListResponse.Error -> {
                                            isLoading = false
                                        }
                                        is ApiListResponse.Idle -> {
                                            isLoading = true
                                        }
                                        is ApiListResponse.Success -> {
                                            posts.clear()
                                            posts.addAll(response.data)
                                            isLoading = false
                                            skip += POST_PER_PAGE
                                            showMore = response.data.size >= POST_PER_PAGE
                                        }
                                    }
                                }
                            }
                        }
                    }
                },
                onClick = {
                    context.router.navigateTo(
                        pathQueryAndFragment = Screen.Post.passPostId(it)
                    )
                },
                useColoredCategoryChips = false,
            )
        } else {
            LoadingIndicator()
        }

        FooterSection()
    }

}