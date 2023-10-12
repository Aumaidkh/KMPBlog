package com.hopcape.blog.components

import androidx.compose.runtime.Composable
import com.hopcape.blog.models.Theme
import com.hopcape.blog.navigation.Screen
import com.hopcape.blog.styles.NavigationItemStyle
import com.hopcape.blog.utils.Constants.FONT_FAMILY
import com.hopcape.blog.utils.Constants.SIDE_PANEL_WIDTH
import com.hopcape.blog.utils.Id.navigationText
import com.hopcape.blog.utils.Id.svgParent
import com.hopcape.blog.utils.Id.vectorIcon
import com.hopcape.blog.utils.Resource
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.dom.Path
import com.varabyte.kobweb.compose.dom.Svg
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh

@Composable
fun SidePanel() {
    val context = rememberPageContext()
    Column(
        modifier = Modifier
            .padding(leftRight = 40.px, topBottom = 50.px   )
            .width(SIDE_PANEL_WIDTH.px)
            .height(100.vh)
            .position(Position.Fixed)
            .backgroundColor(Theme.Secondary.rgb)
            .zIndex(9)
    ) {
        // Icon
        Image(
            modifier = Modifier
                .margin(bottom = 60.px)
                .height(80.px)
                .width(150.px),
            src = Resource.Image.logo,
            desc = "Logo Image"
        )
        // Text
        SpanText(
            modifier = Modifier
                .margin(bottom = 30.px)
                .fontFamily(FONT_FAMILY)
                .fontSize(14.px)
                .color(Theme.HalfWhite.rgb),
            text = "Dashboard"
        )

        NavigationItem(
            modifier = Modifier.margin(bottom = 24.px),
            selected = context.route.path == Screen.AdminHome.route,
            title = "Home",
            icon = Resource.PathIcons.home,
            onClick = {}
        )
        NavigationItem(
            modifier = Modifier.margin(bottom = 24.px),
            selected = context.route.path == Screen.AdminCreate.route,
            title = "Create Post",
            icon = Resource.PathIcons.create,
            onClick = {}
        )
        NavigationItem(
            modifier = Modifier.margin(bottom = 24.px),
            selected = context.route.path == Screen.AdminPosts.route,
            title = "My Posts",
            icon = Resource.PathIcons.posts,
            onClick = {}
        )
        NavigationItem(
            title = "Logout",
            icon = Resource.PathIcons.logout,
            onClick = {}
        )
    }
}

@Composable
fun NavigationItem(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    title: String,
    icon: String,
    onClick: () -> Unit
) {
    Row(
        modifier = NavigationItemStyle
            .toModifier()
            .then(modifier)
            .cursor(Cursor.Pointer)
            .onClick { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        VectorIcon(
            selected = selected,
            pathData = icon,
            modifier = Modifier
                .margin(right = 10.px)
        )
        SpanText(
            modifier = NavigationItemStyle
                .toModifier()
                .id(navigationText)
                .fontFamily(FONT_FAMILY)
                .fontSize(16.px)
                .thenIf(
                    condition = selected,
                    other = Modifier.color(Theme.Primary.rgb)
                ),
            text = title,
        )
    }
}

@Composable
fun VectorIcon(
    selected: Boolean,
    modifier: Modifier,
    pathData: String,
) {
    Svg(
        attrs = NavigationItemStyle
            .toModifier()
            .then(modifier)
            .id(svgParent)
            .width(24.px)
            .height(24.px)
            .toAttrs{
                attr("viewBox","0 0 24 24")
                attr("fill","none")
            }

    ) {
        Path {
            attr("d", pathData)
            if (selected){
                stroke(Theme.Primary.rgb)
            }
            attr("stroke-width", "1")
            attr("stroke-lineCap", "round")
            attr("stroke-lineJoin", "round")
        }
    }
}