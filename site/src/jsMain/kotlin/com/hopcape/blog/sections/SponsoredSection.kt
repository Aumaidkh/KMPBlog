package com.hopcape.blog.sections

import androidx.compose.runtime.Composable
import com.hopcape.blog.components.PostPreview
import com.hopcape.blog.models.PostWithoutDetails
import com.hopcape.blog.models.Theme
import com.hopcape.blog.utils.Constants.FONT_FAMILY
import com.hopcape.blog.utils.Constants.SECTION_PAGE_WIDTH
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.silk.components.icons.fa.FaTag
import com.varabyte.kobweb.silk.components.icons.fa.IconSize
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

@Composable
fun SponsoredSection(
    breakpoint: Breakpoint,
    posts: List<PostWithoutDetails>,
    onClick: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .margin(bottom = 100.px)
            .fillMaxWidth()
            .backgroundColor(Theme.LightGray.rgb),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .maxWidth(SECTION_PAGE_WIDTH.px)
                .margin(topBottom = 50.px),
            contentAlignment = Alignment.TopCenter
        ) {
            SponsoredFeed(
                posts = posts,
                breakpoint = breakpoint,
                onClick = onClick
            )
        }
    }
}

@Composable
fun SponsoredFeed(
    posts: List<PostWithoutDetails>,
    breakpoint: Breakpoint,
    onClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(
                if (breakpoint > Breakpoint.MD) 80.percent else 90.percent
            ),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .margin(bottom = 24.px),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FaTag(
                modifier = Modifier
                    .margin(right = 10.px)
                    .color(Theme.Purple.rgb),
                size = IconSize.XL
            )
            SpanText(
                modifier = Modifier
                    .fontFamily(FONT_FAMILY)
                    .fontWeight(FontWeight.Medium)
                    .color(Theme.Purple.rgb)
                    .fontSize(18.px),
                text = "Sponsored"
            )
        }
        SimpleGrid(
            modifier = Modifier
                .fillMaxWidth(),
            numColumns = numColumns(base = 1, lg = 2)
        ) {
            posts.forEach { post ->
                PostPreview(
                    modifier = Modifier
                        .thenIf(
                            condition = post == posts.first(),
                            other = Modifier
                                .margin(right = 24.px)
                        ),
                    post = post,
                    vertical = breakpoint <= Breakpoint.MD,
                    useColoredCategories = true,
                    thumbnailHeight = 200.px,
                    onClick = onClick
                )
            }
        }
    }
}