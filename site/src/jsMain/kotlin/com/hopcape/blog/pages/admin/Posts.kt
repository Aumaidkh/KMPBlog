package com.hopcape.blog.pages.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.hopcape.blog.components.AdminPageLayout
import com.hopcape.blog.components.Posts
import com.hopcape.blog.components.SearchBar
import com.hopcape.blog.components.SidePanel
import com.hopcape.blog.models.ApiListResponse
import com.hopcape.blog.models.PostWithoutDetails
import com.hopcape.blog.models.Theme
import com.hopcape.blog.styles.SwitchColorPalette
import com.hopcape.blog.utils.Constants
import com.hopcape.blog.utils.Constants.FONT_FAMILY
import com.hopcape.blog.utils.Constants.SIDE_PANEL_WIDTH
import com.hopcape.blog.utils.fetchPosts
import com.hopcape.blog.utils.isUserLoggedIn
import com.hopcape.blog.utils.noBorder
import com.varabyte.kobweb.compose.css.FontWeight
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
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.components.forms.Switch
import com.varabyte.kobweb.silk.components.forms.SwitchSize
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh
import org.jetbrains.compose.web.dom.Button

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
    var selectable by remember {
        mutableStateOf(false)
    }
    var text by remember {
        mutableStateOf("Select")
    }
    val posts = remember {
        mutableStateListOf<PostWithoutDetails>()
    }
    LaunchedEffect(key1 = Unit){
        fetchPosts(skip = 0).also {apiListResponse ->
            when(apiListResponse){
                is ApiListResponse.Error -> println(apiListResponse.message)
                is ApiListResponse.Idle -> {}
                is ApiListResponse.Success -> {
                    posts.addAll(apiListResponse.data)
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
                SearchBar {  }
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
                        checked = selectable,
                        size = SwitchSize.LG,
                        onCheckedChange = {
                            selectable = it
                        },
                        colorScheme = SwitchColorPalette
                    )
                    SpanText(
                        modifier = Modifier
                            .color(if (selectable) Colors.Black else Theme.HalfBlack.rgb),
                        text = text
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
                        .onClick {

                        }
                        .toAttrs()
                ){
                    SpanText(text = "Delete")
                }
            }

            Posts(posts = posts)
        }
    }
}