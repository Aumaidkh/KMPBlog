package com.hopcape.blog.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.hopcape.blog.models.Theme
import com.hopcape.blog.utils.Id
import com.hopcape.blog.utils.noBorder
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onFocusIn
import com.varabyte.kobweb.compose.ui.modifiers.onFocusOut
import com.varabyte.kobweb.compose.ui.modifiers.onKeyDown
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.icons.fa.FaMagnifyingGlass
import com.varabyte.kobweb.silk.components.icons.fa.IconSize
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Input

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    breakpoint: Breakpoint,
    fullWidth: Boolean = true,
    darkTheme: Boolean = false,
    onSearchIconClicked: (Boolean) -> Unit,
    onEnterClicked: () -> Unit,
) {
    var focused by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(breakpoint){
        if (breakpoint >= Breakpoint.SM){
            onSearchIconClicked(false)
        }
    }

    if (breakpoint >= Breakpoint.SM || fullWidth) {
        Row(
            modifier = modifier
                .thenIf(
                    condition = fullWidth,
                    other = Modifier.fillMaxWidth()
                )
                .height(54.px)
                .backgroundColor(
                    if (darkTheme) {
                        Theme.Tertiary.rgb
                    } else {
                        Theme.LightGray.rgb
                    }
                )
                .borderRadius(r = 100.px)
                .border(
                    width = 2.px,
                    style = LineStyle.Solid,
                    color = if (focused && !darkTheme) Theme.Primary.rgb
                    else if (focused && darkTheme) Theme.Primary.rgb
                    else if (!focused && !darkTheme) Theme.Tertiary.rgb
                    else if (!focused && darkTheme) Theme.Secondary.rgb
                    else Theme.LightGray.rgb
                )
            .transition(
                CSSTransition(
                    property = "all",
                    duration = 300.ms
                )
            )
            .padding(leftRight = 20.px),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FaMagnifyingGlass(
            modifier = Modifier
                .color(if (focused) Theme.Primary.rgb else Theme.DarkGray.rgb)
                .margin(right =  14.px),
            size = IconSize.SM
        )
        Input(
            attrs = Modifier
                .id(Id.searchInput)
                .fillMaxSize()
                .color(
                    color = if (darkTheme) Colors.White else Colors.Black
                )
                .backgroundColor(Colors.Transparent)
                .noBorder()
                .onKeyDown {
                    if (it.key == "Enter"){
                        onEnterClicked()
                    }
                }
                .onFocusIn { focused = true }
                .onFocusOut { focused = false }
                .toAttrs {
                    attr("placeholder", "Search...")
                },
            type = InputType.Text
        )
        }
    } else {
        FaMagnifyingGlass(
            modifier = Modifier
                .color(Theme.Primary.rgb)
                .cursor(Cursor.Pointer)
                .onClick {
                    onSearchIconClicked(true)
                }
                .margin(right =  14.px),
            size = IconSize.SM
        )
    }
}