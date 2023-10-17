package com.hopcape.blog.pages.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.hopcape.blog.components.AdminPageLayout
import com.hopcape.blog.components.MessageBarPopup
import com.hopcape.blog.components.Posts
import com.hopcape.blog.components.SearchBar
import com.hopcape.blog.models.ApiListResponse
import com.hopcape.blog.models.Message
import com.hopcape.blog.models.PostWithoutDetails
import com.hopcape.blog.models.Theme
import com.hopcape.blog.navigation.Screen
import com.hopcape.blog.styles.SwitchColorPalette
import com.hopcape.blog.utils.Constants.FONT_FAMILY
import com.hopcape.blog.utils.Constants.POST_PER_PAGE
import com.hopcape.blog.utils.Constants.QUERY_PARAM
import com.hopcape.blog.utils.Constants.SIDE_PANEL_WIDTH
import com.hopcape.blog.utils.Id
import com.hopcape.blog.utils.deletePosts
import com.hopcape.blog.utils.fetchPosts
import com.hopcape.blog.utils.isUserLoggedIn
import com.hopcape.blog.utils.noBorder
import com.hopcape.blog.utils.parseSwitchText
import com.hopcape.blog.utils.searchPostsByTitle
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TransitionProperty
import com.varabyte.kobweb.compose.css.Visibility
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.visibility
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.forms.Switch
import com.varabyte.kobweb.silk.components.forms.SwitchSize
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.browser.document
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Button
import org.w3c.dom.HTMLInputElement

@Page
@Composable
fun PostsPage() {
    isUserLoggedIn {
        PostsScreen()
    }
}

@Composable
fun PostsScreen() {
    val breakPoint = rememberBreakpoint()
    val context = rememberPageContext()
    var selectableMode by remember {
        mutableStateOf(false)
    }
    var postToSkip by remember { mutableStateOf(0) }
    var showMoreVisibility by remember { mutableStateOf(false) }
    val selectedPosts = remember {
        mutableStateListOf<String>()
    }
    var switchText by remember(selectedPosts.size) {
        mutableStateOf(parseSwitchText(selectedPosts))
    }
    val posts = remember {
        mutableStateListOf<PostWithoutDetails>()
    }

    var message by remember {
        mutableStateOf<Message?>(null)
    }

    var hasParams = remember(context.route) {
        context.route.params.containsKey(QUERY_PARAM)
    }
    var query = remember(context.route) {
        try {
            context.route.params.getValue(QUERY_PARAM)
        } catch (e: Exception){
            ""
        }
    }

    val scope = rememberCoroutineScope()
    postToSkip = 0
    LaunchedEffect(key1 = context.route){
        if (hasParams){
            (document.getElementById(Id.searchInput) as HTMLInputElement).value = query.replace("%20"," ")
            searchPostsByTitle(
                query = query,
                skip = postToSkip
            ).also { result ->
                when(result){
                    is ApiListResponse.Error -> println(result.message)
                    is ApiListResponse.Idle -> {}
                    is ApiListResponse.Success -> {
                        posts.clear()
                        posts.addAll(result.data)
                        postToSkip += POST_PER_PAGE
                        showMoreVisibility = result.data.size >= POST_PER_PAGE
                    }
                }
            }
        } else {
            fetchPosts(
                skip = postToSkip).also {apiListResponse ->
                when(apiListResponse){
                    is ApiListResponse.Error -> println(apiListResponse.message)
                    is ApiListResponse.Idle -> {}
                    is ApiListResponse.Success -> {
                        posts.clear()
                        posts.addAll(apiListResponse.data)
                        postToSkip += POST_PER_PAGE
                        showMoreVisibility = apiListResponse.data.size >= POST_PER_PAGE
                    }
                }
            }
        }

    }

    AdminPageLayout {
        Column(
            modifier = Modifier
                .margin(topBottom = 50.px)
                .fillMaxSize()
                .padding(left = if (breakPoint > Breakpoint.MD) SIDE_PANEL_WIDTH.px else 0.px),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(
                        if (breakPoint > Breakpoint.MD) 30.percent
                        else 50.percent
                    )
                    .margin(bottom = 24.px),
                contentAlignment = Alignment.Center
            ) {
                SearchBar(
                    modifier = Modifier
                        .transition(CSSTransition(property = TransitionProperty.All,duration = 300.ms))
                        .visibility(
                            if (selectableMode) Visibility.Hidden else Visibility.Visible
                        )

                ) {
                    val query = (document.getElementById(Id.searchInput) as HTMLInputElement).value
                    context.router
                        .navigateTo(
                            pathQueryAndFragment = if (query.isNotEmpty()) Screen.AdminPosts.searchByTitle(query) else Screen.AdminPosts.route
                        )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(if (breakPoint > Breakpoint.MD) 80.percent else 90.percent)
                    .margin(bottom = 24.px),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Switch(
                        modifier = Modifier
                            .margin(right = 8.px),
                        checked = selectableMode,
                        size = SwitchSize.LG,
                        onCheckedChange = {
                            selectableMode = it
                            if (!selectableMode){
                                switchText = "Select"
                                // We want to clear the selected posts list when the switched is turned off
                                selectedPosts.clear()
                            } else {
                                switchText = "${selectedPosts.size} Posts selected"
                            }
                        },
                        colorScheme = SwitchColorPalette
                    )
                    SpanText(
                        modifier = Modifier
                            .color(if (selectableMode) Colors.Black else Theme.HalfBlack.rgb),
                        text = switchText
                    )
                }

                Button(
                    attrs = Modifier
                        .height(54.px)
                        .margin(right = 20.px)
                        .padding(leftRight = 24.px)
                        .backgroundColor(Theme.Red.rgb)
                        .color(Colors.White)
                        .noBorder()
                        .borderRadius(4.px)
                        .fontFamily(FONT_FAMILY)
                        .fontSize(14.px)
                        .fontWeight(FontWeight.Medium)
                        .visibility(if (selectedPosts.isNotEmpty()) Visibility.Visible else Visibility.Hidden)
                        .onClick {
                           scope.launch {
                               deletePosts(postIds = selectedPosts).also {
                                   if (it){
                                       selectableMode = false
                                       switchText = "Select"
                                       postToSkip -= selectedPosts.size
                                       posts.removeAll { post ->
                                           post._id in selectedPosts
                                       }
                                       selectedPosts.clear()
                                       message = Message.Success("Items Deleted")
                                   } else {
                                       message = Message.Error("Can't delete items")
                                   }
                               }
                           }
                        }
                        .toAttrs()
                ){
                    SpanText(text = "Delete")
                }
            }

            Posts(
                posts = posts,
                breakpoint = breakPoint,
                showMoreVisibility = showMoreVisibility,
                onShowMore = {
                    scope.launch {
                        if (hasParams){
                            searchPostsByTitle(
                                query = query,
                                skip = postToSkip
                            ).also { result ->
                                when(result){
                                    is ApiListResponse.Error -> println(result.message)
                                    is ApiListResponse.Idle -> {}
                                    is ApiListResponse.Success -> {
                                        posts.clear()
                                        posts.addAll(result.data)
                                        postToSkip += POST_PER_PAGE
                                        showMoreVisibility = result.data.size >= POST_PER_PAGE
                                    }
                                }
                            }
                        } else {
                            fetchPosts(
                                skip = postToSkip).also {apiListResponse ->
                                when(apiListResponse){
                                    is ApiListResponse.Error -> println(apiListResponse.message)
                                    is ApiListResponse.Idle -> {}
                                    is ApiListResponse.Success -> {
                                        if (apiListResponse.data.isNotEmpty()){
                                            posts.addAll(apiListResponse.data)
                                            postToSkip += POST_PER_PAGE
                                            showMoreVisibility = apiListResponse.data.size > POST_PER_PAGE
                                        } else {
                                            showMoreVisibility = false
                                        }
                                    }
                                }
                            }
                        }
                    }
                },
                selectableMode = selectableMode,
                onDeselect = {
                    selectedPosts.remove(it)
                },
                onSelect = {
                    selectedPosts.add(it)
                }
            )
        }
    }

    if (message != null){
        MessageBarPopup(
            message = message!!,
            onDialogDismissed = {
                message = null
            }
        )
    }
}