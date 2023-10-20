package com.hopcape.blog.sections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.hopcape.blog.components.CategoryNavigationItems
import com.hopcape.blog.components.SearchBar
import com.hopcape.blog.models.Category
import com.hopcape.blog.models.Theme
import com.hopcape.blog.styles.CategoryItemStyle
import com.hopcape.blog.utils.Constants.FONT_FAMILY
import com.hopcape.blog.utils.Constants.HEADER_HEIGHT
import com.hopcape.blog.utils.Constants.SECTION_PAGE_WIDTH
import com.hopcape.blog.utils.Resource
import com.hopcape.blog.utils.isLaptop
import com.hopcape.blog.utils.isLargeDevice
import com.hopcape.blog.utils.isMobile
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.fa.FaBars
import com.varabyte.kobweb.silk.components.icons.fa.FaXmark
import com.varabyte.kobweb.silk.components.icons.fa.IconSize
import com.varabyte.kobweb.silk.components.navigation.Link
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

@Composable
fun HeaderSection(
    breakpoint: Breakpoint,
    onMenuOpened: () -> Unit
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
                .backgroundColor(Theme.Secondary.rgb)
                .maxWidth(SECTION_PAGE_WIDTH.px),
            contentAlignment = Alignment.TopCenter
        ) {
            Header(
                breakpoint = breakpoint,
                onMenuClicked = onMenuOpened
            )
        }
    }
}

@Composable
fun Header(
    breakpoint: Breakpoint,
    onMenuClicked: () -> Unit
) {
    var fullSearchBar by remember {
        mutableStateOf(false)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth(
                if (breakpoint.isLaptop()) 80.percent else 90.percent
            )
            .height(HEADER_HEIGHT.px),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (breakpoint <= Breakpoint.MD){
            if (fullSearchBar){
                FaXmark(
                    modifier = Modifier
                        .margin(right = 25.px)
                        .color(Colors.White)
                        .cursor(Cursor.Pointer)
                        .onClick {
                           fullSearchBar = false
                        },
                    size = IconSize.XL
                )
            } else {
                FaBars(
                    modifier = Modifier
                        .margin(right = 25.px)
                        .color(Colors.White)
                        .cursor(Cursor.Pointer)
                        .onClick { onMenuClicked() },
                    size = IconSize.XL
                )
            }
        }
        // Logo
        if (!fullSearchBar){
            Image(
                modifier = Modifier
                    .margin(right = 50.px)
                    .width(if (breakpoint.isMobile()) 70.px else 100.px)
                    .cursor(Cursor.Pointer)
                    .onClick {  },
                src = Resource.Image.logo.removePrefix("/"),
                desc = "Logo"
            )
        }
        // Categories
        if (breakpoint.isLargeDevice()){
            CategoryNavigationItems()
        }
        Spacer()
        SearchBar(
            breakpoint = breakpoint,
            darkTheme = true,
            fullWidth = fullSearchBar,
            onSearchIconClicked = {
                 fullSearchBar = it
            },
            onEnterClicked = {}
        )
    }
}

