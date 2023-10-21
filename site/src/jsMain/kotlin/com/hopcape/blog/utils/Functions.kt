package com.hopcape.blog.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.hopcape.blog.models.ControlStyle
import com.hopcape.blog.models.EditorControl
import com.hopcape.blog.navigation.Screen
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.outline
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.text.SpanText
import kotlinx.browser.document
import kotlinx.browser.localStorage
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px
import org.w3c.dom.HTMLTextAreaElement
import org.w3c.dom.get
import org.w3c.dom.set
import kotlin.js.Date

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
            style = LineStyle.Solid
        )
        .outline(
            width = 0.px,
            color = Colors.Transparent,
            style = LineStyle.Solid
        )
}

fun getEditor() = (document.getElementById(Id.editor) as HTMLTextAreaElement)

fun getSelectedIntRange(): IntRange? {
    val editor = getEditor()
    val start = editor.selectionStart
    val end = editor.selectionEnd

    return if (start != null && end != null){
        IntRange(start,(end-1))
    } else {
        null
    }
}
fun getSelectedText(): String? {
    val editor = getEditor()
    val range = getSelectedIntRange()
    return if (range != null){
        editor.value.substring(range)
    } else {
        null
    }
}

fun applyStyle(controlStyle: ControlStyle){
    val selectedText = getSelectedText()
    val selectedIntRange = getSelectedIntRange()
    if (selectedIntRange != null && selectedText != null){
        getEditor().value = getEditor().value.replaceRange(
            range = selectedIntRange,
            replacement = controlStyle.style
        )
        document.getElementById(Id.editorPreview)?.innerHTML = getEditor().value
    }
}

fun applyControlStyle(
    control: EditorControl,
    onLinkClicked: () -> Unit,
    onImageClicked: () -> Unit
){
    when(control){
        EditorControl.Bold -> {
            applyStyle(
                controlStyle = ControlStyle.Bold(
                    selectedText = getSelectedText()
                )
            )
        }
        EditorControl.Italic -> {
            applyStyle(
                controlStyle = ControlStyle.Italic(
                    selectedText = getSelectedText()
                )
            )
        }
        EditorControl.Link -> {
            onLinkClicked()
        }
        EditorControl.Title -> {
            applyStyle(
                controlStyle = ControlStyle.Title(
                    selectedText = getSelectedText()
                )
            )
        }
        EditorControl.Subtitle -> {
            applyStyle(
                controlStyle = ControlStyle.Subtitle(
                    selectedText = getSelectedText()
                )
            )
        }
        EditorControl.Quote -> {
            applyStyle(
                controlStyle = ControlStyle.Quote(
                    selectedText = getSelectedText()
                )
            )
        }
        EditorControl.Code -> {
            applyStyle(
                controlStyle = ControlStyle.CodeBlock(
                    selectedText = getSelectedText()
                )
            )
        }
        EditorControl.Image -> {
            onImageClicked()
        }
    }
}

fun Long.parseDateString() = Date(this).toLocaleDateString()

fun parseSwitchText(posts: List<String>): String {
    return if (posts.isEmpty()) "Select" else if (posts.size == 1) "1 Post Selected" else "${posts.size} Posts Selected"
}

fun isValidEmail(email: String): Boolean {
    val regex ="^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$"
    return regex.toRegex().matches(email)
}

fun Breakpoint.isLaptop() = this > Breakpoint.MD
fun Breakpoint.isMobile() = this < Breakpoint.SM

fun Breakpoint.isLargeDevice() = this >= Breakpoint.LG