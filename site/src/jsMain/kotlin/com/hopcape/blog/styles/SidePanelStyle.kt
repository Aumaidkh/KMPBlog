package com.hopcape.blog.styles

import com.hopcape.blog.models.Theme
import com.hopcape.blog.utils.Id.navigationText
import com.hopcape.blog.utils.Id.svgParent
import com.hopcape.blog.utils.Id.vectorIcon
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.TransitionProperty
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import org.jetbrains.compose.web.css.ms

val NavigationItemStyle by ComponentStyle {
    cssRule(" > #$svgParent"){
        Modifier
            .transition(CSSTransition(
                property = TransitionProperty.All,
                duration = 300.ms
            ))
            .styleModifier {
                property("stroke",Theme.White.hex)
            }
    }
    cssRule(":hover > #$svgParent"){
        Modifier
            .styleModifier {
            property("stroke",Theme.Primary.hex)
        }
    }
    cssRule(":hover > #$navigationText"){
        Modifier
            .transition(CSSTransition(
                property = TransitionProperty.All,
                duration = 300.ms
            ))
            .color(Theme.Primary.rgb)
    }
    cssRule(" > #navigationText"){
        Modifier.color(Theme.White.rgb)
    }
}