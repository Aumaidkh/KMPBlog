package com.hopcape.blog.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.hopcape.blog.utils.Constants.SECTION_PAGE_WIDTH
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import org.jetbrains.compose.web.css.px

@Composable
fun AdminPageLayout(
    content: @Composable () -> Unit
) {
    var overFlowMenuOpened by remember {
        mutableStateOf(false)
    }
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .maxWidth(SECTION_PAGE_WIDTH.px)
        ) {
            SidePanel(
                onMenuClick = {
                    overFlowMenuOpened = true
                }
            )
            if (overFlowMenuOpened){
                OverflowSidePanel(
                    onMenuClose = {
                        overFlowMenuOpened = false
                    }
                )
            }
            content()
        }
    }
}