package com.hopcape.blog.pages.posts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.hopcape.blog.components.CategoryNavigationItems
import com.hopcape.blog.components.ErrorView
import com.hopcape.blog.components.OverflowSidePanel
import com.hopcape.blog.models.ApiResponse
import com.hopcape.blog.models.Constants.QUERY_POST_ID
import com.hopcape.blog.models.Post
import com.hopcape.blog.models.Theme
import com.hopcape.blog.sections.FooterSection
import com.hopcape.blog.sections.HeaderSection
import com.hopcape.blog.utils.Constants.FONT_FAMILY
import com.hopcape.blog.utils.Constants.SECTION_PAGE_WIDTH
import com.hopcape.blog.utils.Id
import com.hopcape.blog.utils.Resource
import com.hopcape.blog.utils.fetchPostBy
import com.hopcape.blog.utils.isMobile
import com.hopcape.blog.utils.parseDateString
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextOverflow
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxHeight
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.textOverflow
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.browser.document
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh
import org.jetbrains.compose.web.dom.Div
import org.w3c.dom.HTMLDivElement

@Page(routeOverride = "/")
@Composable
fun PostsPage() {
    val context = rememberPageContext()
    val scope = rememberCoroutineScope()
    val breakpoint = rememberBreakpoint()
    var overFlowMenuOpened by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val hasPostId by remember(context.route) { mutableStateOf(context.route.params.containsKey(QUERY_POST_ID)) }
    val post = remember { mutableStateOf<Post?>(null) }
    LaunchedEffect(key1 = context.route){
        if (hasPostId){
            val query = context.route.params[QUERY_POST_ID]
            query?.let {
                fetchPostBy(it).also { response ->
                    when(response){
                        is ApiResponse.Error -> {
                            errorMessage = response.message
                        }
                        is ApiResponse.Idle -> {}
                        is ApiResponse.Success -> {
                            post.value = response.data
                            scope.launch {
                                delay(50)
                                try {
                                    js("hljs.highlightAll()") as Unit
                                }catch (e: Exception){
                                    println(e.message)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .maxHeight(100.vh),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        if (overFlowMenuOpened){
            OverflowSidePanel(
                onMenuClose = {
                    overFlowMenuOpened = false
                },
                content = {
                    CategoryNavigationItems(
                        vertical = true,
                    )
                }
            )
        }
        HeaderSection(
            breakpoint = breakpoint,
            onMenuOpened = {
                overFlowMenuOpened = true
            },
            logo = Resource.Image.blogLogo
        )
        errorMessage?.let {
            ErrorView(message = "Can't find post with the given id")
        } ?: post.value?.let {
            PostContent(
                post = it,
                breakpoint = breakpoint
            )
        }

        FooterSection()
    }
}

@Composable
fun PostContent(
    post: Post,
    breakpoint: Breakpoint
) {
    LaunchedEffect(post){
        (document.getElementById(Id.postContent) as HTMLDivElement).innerHTML = post.content
    }
    Column(
        modifier = Modifier
            .margin(top = 50.px, bottom = 200.px)
            .padding(leftRight = 24.px)
            .fillMaxWidth()
            .maxWidth(800.px),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SpanText(
            modifier = Modifier
                .fillMaxWidth()
                .fontFamily(FONT_FAMILY)
                .fontSize(14.px)
                .color(Theme.HalfBlack.rgb),
            text = post.date.parseDateString()
        )
        SpanText(
            modifier = Modifier
                .fillMaxWidth()
                .margin(bottom = 20.px)
                .fontFamily(FONT_FAMILY)
                .fontSize(
                    value = if (breakpoint <= Breakpoint.SM) 20.px else 40.px
                )
                .textOverflow(TextOverflow.Ellipsis)
                .styleModifier {
                    property("display", "-webkit-box")
                    property("-webkit-line-clamp", "2")
                    property("line-clamp", "2")
                    property("-webkit-box-orient", "vertical")
                }
                .fontWeight(FontWeight.Bold)
                .color(Colors.Black),
            text = post.title
        )
        Image(
            modifier = Modifier
                .margin(bottom = 40.px)
                .maxHeight(600.px)
                .fillMaxWidth(),
            src = post.thumbnail
        )
        Div(
            attrs = Modifier
                .id(Id.postContent)
                .fillMaxWidth()
                .fontFamily(FONT_FAMILY)
                .toAttrs()
        ) {

        }
    }
}