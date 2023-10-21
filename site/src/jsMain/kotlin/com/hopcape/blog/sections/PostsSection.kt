package com.hopcape.blog.sections

import androidx.compose.runtime.Composable
import com.hopcape.blog.components.PostsView
import com.hopcape.blog.models.PostWithoutDetails
import com.hopcape.blog.utils.Constants.SECTION_PAGE_WIDTH
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import org.jetbrains.compose.web.css.px

@Composable
fun PostsSection(
    breakpoint: Breakpoint,
    posts: List<PostWithoutDetails>,
    title: String? = null,
    showMoreVisibility: Boolean,
    onShowMore: () -> Unit,
    onClick: (postId: String) -> Unit,
    useColoredCategoryChips: Boolean = true
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .margin(topBottom = 50.px)
            .maxWidth(SECTION_PAGE_WIDTH.px),
        contentAlignment = Alignment.TopCenter
    ) {
        PostsView(
            breakpoint = breakpoint,
            title = title,
            showMoreVisibility = showMoreVisibility,
            onShowMore = onShowMore,
            posts = posts,
            onClick = onClick,
            useColoredCategoryChips = useColoredCategoryChips
        )
    }

}