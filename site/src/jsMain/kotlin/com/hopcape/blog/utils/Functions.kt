package com.hopcape.blog.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.hopcape.blog.navigation.Screen
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.outline
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.text.SpanText
import kotlinx.browser.localStorage
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px
import org.w3c.dom.get
import org.w3c.dom.set

@Composable
fun isUserLoggedIn(
    content: @Composable () -> Unit
) {
    val context = rememberPageContext()
    val remembered = remember{ localStorage["remember"].toBoolean()}
    val userId = remember{ localStorage["userId"]}
    var userIdExists by remember{ mutableStateOf(false) }

    LaunchedEffect(key1 = Unit ){
        userIdExists = if (!userId.isNullOrEmpty()) checkUserId(userId) else false
        if (!remembered || !userIdExists){
            context.router.navigateTo(Screen.AdminLogin.route)
        }
    }
    if (remembered && userIdExists) {
        content()
    } else {
        Box{
           SpanText("User Not Exists")
        }
    }
}

fun logout(){
    localStorage["remember"] = "false"
    localStorage["userId"] = ""
    localStorage["username"] = ""
}

fun Modifier.noBorder(): Modifier {
    return this
        .border(
            width = 0.px,
            color = Colors.Transparent,
            style = LineStyle.None
        )
        .outline(
            width = 0.px,
            color = Colors.Transparent,
            style = LineStyle.None
        )
}