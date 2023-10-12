package com.hopcape.blog.pages.admin

import androidx.compose.runtime.Composable
import com.hopcape.blog.components.SidePanel
import com.hopcape.blog.utils.Constants
import com.hopcape.blog.utils.isUserLoggedIn
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.core.Page
import org.jetbrains.compose.web.css.px

@Page
@Composable
fun CreatePage() {
    isUserLoggedIn {
        CreateScreen()
    }
}

@Composable
fun CreateScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Row(
            modifier = Modifier
                .fillMaxSize()
                .maxWidth(Constants.SECTION_PAGE_WIDTH.px)
        ) {
            SidePanel()
        }
    }
}