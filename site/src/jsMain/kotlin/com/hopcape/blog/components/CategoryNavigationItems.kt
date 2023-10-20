package com.hopcape.blog.components

import androidx.compose.runtime.Composable
import com.hopcape.blog.models.Category
import com.hopcape.blog.styles.CategoryItemStyle
import com.hopcape.blog.utils.Constants
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.silk.components.navigation.Link
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.px

@Composable
fun CategoryNavigationItems(vertical: Boolean = false) {
    Category.values().forEach { category ->
        Link(
            modifier = CategoryItemStyle
                .toModifier()
                .thenIf(
                    condition = vertical,
                    other = Modifier
                        .margin(bottom = 24.px)
                )
                .thenIf(
                    condition = !vertical,
                    other = Modifier
                        .margin(right = 24.px)
                )
                .fontFamily(Constants.FONT_FAMILY)
                .fontSize(16.px)
                .fontWeight(FontWeight.Medium)
                .textDecorationLine(TextDecorationLine.None)
                .onClick {  },
            path = "",
            text = category.name
        )
    }
}