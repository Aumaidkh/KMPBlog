package com.hopcape.blog.sections

import androidx.compose.runtime.Composable
import com.hopcape.blog.components.PostPreview
import com.hopcape.blog.models.ApiListResponse
import com.hopcape.blog.models.PostWithoutDetails
import com.hopcape.blog.models.Theme
import com.hopcape.blog.navigation.Screen
import com.hopcape.blog.utils.Constants.SECTION_PAGE_WIDTH
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import org.jetbrains.compose.web.css.AlignContent
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

@Composable
fun MainSection(
    response : ApiListResponse,
    breakpoint: Breakpoint
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .backgroundColor(Theme.Secondary.rgb),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .maxWidth(SECTION_PAGE_WIDTH.px)
                .backgroundColor(Theme.Secondary.rgb),
            contentAlignment = Alignment.Center
        ) {
            when(response){
                is ApiListResponse.Error -> {}
                ApiListResponse.Idle -> {}
                is ApiListResponse.Success -> MainPosts(
                    posts = response.data,
                    breakpoint = breakpoint
                )
            }
        }
    }
}

@Composable
fun MainPosts(
    posts: List<PostWithoutDetails>,
    breakpoint: Breakpoint
) {
    val context = rememberPageContext()
    Row(
        modifier = Modifier
            .fillMaxWidth(
                if (breakpoint > Breakpoint.MD) 80.percent else 90.percent // The row will take 90 percent on smaller devices
            )
            .margin(topBottom = 50.px)
    ) {
        if (breakpoint == Breakpoint.XL){
            PostPreview(
                thumbnailHeight = 640.px,
                darkTheme = true,
                post = posts.first(),
                onClick = {
                    val postId = posts.first()._id
                    context.router.navigateTo(
                        pathQueryAndFragment = Screen.Post.passPostId(postId)
                    )
                }
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth(55.percent)
                    .margin(left = 50.px)
            ) {
                posts.drop(1).forEach { post ->
                    PostPreview(
                        thumbnailHeight = 200.px,
                        darkTheme = true,
                        post = post,
                        vertical = false,
                        onClick = {
                            val postId = post._id
                            context.router.navigateTo(
                                pathQueryAndFragment = Screen.Post.passPostId(postId)
                            )
                        }
                    )
                }
            }
        } else if (breakpoint >= Breakpoint.MD){
            Box(
                modifier = Modifier
                    .fillMaxWidth(50.percent)
                    .margin(right = 24.px),
            ) {
                PostPreview(
                    darkTheme = true,
                    post = posts.first(),
                    onClick = {
                        val postId = posts.first()._id
                        context.router.navigateTo(
                            pathQueryAndFragment = Screen.Post.passPostId(postId)
                        )
                    }
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth(50.percent)
                    .margin(left = 24.px),
            ) {
                PostPreview(
                    darkTheme = true,
                    post = posts[1],
                    onClick = {
                        val postId = posts[1]._id
                        context.router.navigateTo(
                            pathQueryAndFragment = Screen.Post.passPostId(postId)
                        )
                    }
                )
            }

        } else {
            PostPreview(
                darkTheme = true,
                post = posts.first(),
                onClick = {
                    val postId = posts.first()._id
                    context.router.navigateTo(
                        pathQueryAndFragment = Screen.Post.passPostId(postId)
                    )
                }
            )
        }
    }
}