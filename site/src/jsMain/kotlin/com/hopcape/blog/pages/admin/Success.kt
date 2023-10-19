package com.hopcape.blog.pages.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.hopcape.blog.models.Constants.POST_UPDATED_PARAM
import com.hopcape.blog.models.Theme
import com.hopcape.blog.navigation.Screen
import com.hopcape.blog.utils.Constants.FONT_FAMILY
import com.hopcape.blog.utils.Resource
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.text.SpanText
import kotlinx.coroutines.delay
import org.jetbrains.compose.web.css.px

@Composable
@Page
fun SuccessPage() {
    val context = rememberPageContext()
    val hasParams by remember(context.route) {
        mutableStateOf(context.route.params.containsKey(POST_UPDATED_PARAM))
    }

    var message by remember {
        mutableStateOf("Post Created Successfully")
    }

    LaunchedEffect(key1 = Unit){
        // Resolve message
        if (hasParams){
            context.route.params[POST_UPDATED_PARAM]?.toBoolean()?.let {
                if (it){
                   message = "Post Updated Successfully"
                }
            }
        }

        delay(5000)
        context.router.navigateTo(
            pathQueryAndFragment = Screen.AdminCreate.route
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icon
        Image(
            modifier = Modifier
                .margin(bottom = 24.px)
                .size(100.px),
            src = Resource.Icon.success
        )
        // Text
        SpanText(
            modifier = Modifier
                .margin(bottom = 8.px)
                .fontFamily(FONT_FAMILY)
                .fontSize(24.px)
                .fontWeight(FontWeight.Medium)
                .color(Colors.Black),
            text = message
        )
        // Text
        SpanText(
            modifier = Modifier
                .fontFamily(FONT_FAMILY)
                .fontSize(16.px)
                .fontWeight(FontWeight.Normal)
                .color(Theme.HalfBlack.rgb),
            text = "Redirecting you back..."
        )
    }
}