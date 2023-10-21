package com.hopcape.blog.styles

import com.hopcape.blog.models.NewsLetter
import com.hopcape.blog.models.Theme
import com.hopcape.blog.utils.noBorder
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.TransitionProperty
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.outline
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.focus
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.px

val NewsLetterStyle by ComponentStyle {
    base {
        Modifier
            .noBorder()
            .transition(
                CSSTransition(
                    property = TransitionProperty.All,
                    duration = 300.ms
                )
            )
    }
    focus {
        Modifier
            .outline(
                width = 1.px,
                style = LineStyle.Solid,
                color = Theme.Primary.rgb
            )
            .border(
                width = 1.px,
                style = LineStyle.Solid,
                color = Theme.Primary.rgb
            )
            .transition(
                CSSTransition(
                    property = TransitionProperty.All,
                    duration = 300.ms
                )
            )
    }
}