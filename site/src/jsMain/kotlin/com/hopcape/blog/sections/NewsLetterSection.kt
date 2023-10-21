package com.hopcape.blog.sections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.hopcape.blog.components.MessageBarPopup
import com.hopcape.blog.models.Message
import com.hopcape.blog.models.NewsLetter
import com.hopcape.blog.models.Theme
import com.hopcape.blog.styles.NewsLetterStyle
import com.hopcape.blog.utils.Constants.FONT_FAMILY
import com.hopcape.blog.utils.Constants.SECTION_PAGE_WIDTH
import com.hopcape.blog.utils.Id
import com.hopcape.blog.utils.isValidEmail
import com.hopcape.blog.utils.noBorder
import com.hopcape.blog.utils.subscribeNewsletter
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import kotlinx.browser.document
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Input
import org.w3c.dom.HTMLInputElement

@Composable
fun NewsLetterSection(
    breakpoint: Breakpoint
) {
    var responseMessage by remember { mutableStateOf<String?>(null) }
    var invalidEmailPopup by remember { mutableStateOf(false) }

    if (invalidEmailPopup){
        MessageBarPopup(
            breakpoint = breakpoint,
            message = Message.Error("Invalid Email"),
            onDialogDismissed = {
                invalidEmailPopup = false
            }
        )
    }

    responseMessage?.let {
        MessageBarPopup(
            breakpoint = breakpoint,
            message = Message.Success(it),
            onDialogDismissed = {
                responseMessage = null
            }
        )
    }

    Box(
        modifier = Modifier
            .margin(bottom = 250.px)
            .fillMaxWidth()
            .maxWidth(SECTION_PAGE_WIDTH.px)
    ) {
        NewsLetterContent(
            breakpoint = breakpoint,
            onSubscribe = {
                responseMessage = it
            },
            onInvalidEmail = {
                invalidEmailPopup = true
            }
        )
    }
}

@Composable
fun NewsLetterContent(
    breakpoint: Breakpoint,
    onSubscribe: (String) -> Unit,
    onInvalidEmail: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        SpanText(
            modifier = Modifier
                .fillMaxWidth()
                .fontFamily(FONT_FAMILY)
                .fontWeight(FontWeight.Bold)
                .textAlign(TextAlign.Center)
                .fontSize(36.px),
            text = "Don't miss any New Post"
        )
        SpanText(
            modifier = Modifier
                .fillMaxWidth()
                .fontFamily(FONT_FAMILY)
                .fontWeight(FontWeight.Bold)
                .textAlign(TextAlign.Center)
                .fontSize(36.px),
            text = "Sign up to our Newsletter!"
        )
        SpanText(
            modifier = Modifier
                .margin(top = 6.px)
                .fillMaxWidth()
                .fontFamily(FONT_FAMILY)
                .fontWeight(FontWeight.Normal)
                .textAlign(TextAlign.Center)
                .color(Theme.HalfBlack.rgb)
                .fontSize(18.px),
            text = "Keep up with the latest news and blogs"
        )
        if (breakpoint > Breakpoint.SM) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .margin(top = 40.px),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SubscriptionForm(
                    vertical = false,
                    onSubscribe = onSubscribe,
                    onInvalidEmail = onInvalidEmail
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .margin(top = 40.px),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                SubscriptionForm(
                    vertical = true,
                    onSubscribe = onSubscribe,
                    onInvalidEmail = onInvalidEmail
                )
            }
        }
    }
}

@Composable
fun SubscriptionForm(
    vertical: Boolean,
    onSubscribe: (String) -> Unit,
    onInvalidEmail: () -> Unit
) {
    val scope = rememberCoroutineScope()
    Input(
        attrs = NewsLetterStyle
            .toModifier()
            .id(Id.emailInput)
            .width(320.px)
            .height(54.px)
            .color(Theme.DarkGray.rgb)
            .backgroundColor(Theme.Gray.rgb)
            .padding(leftRight = 24.px)
            .margin(
                right = if (vertical) 0.px else 20.px,
                bottom = if (vertical) 20.px else 0.px,
            )
            .fontFamily(FONT_FAMILY)
            .fontSize(16.px)
            .borderRadius(r = 100.px)
            .toAttrs {
                attr("placeholder", "Your Email Address")
            },
        type = InputType.Text
    )
    Button(
        attrs = Modifier
            .onClick {
                val email = (document.getElementById(Id.emailInput) as HTMLInputElement).value
                if (isValidEmail(email)){
                    scope.launch {
                        onSubscribe(
                            subscribeNewsletter(
                                newsLetter = NewsLetter(
                                    email = email
                                )
                            )
                        )
                    }
                } else {
                    onInvalidEmail()
                }
            }
            .height(54.px)
            .borderRadius(r = 100.px)
            .backgroundColor(Theme.Primary.rgb)
            .padding(leftRight = 50.px)
            .noBorder()
            .cursor(Cursor.Pointer)
            .toAttrs()
    ) {
        SpanText(
            modifier = Modifier
                .fontFamily(FONT_FAMILY)
                .fontSize(18.px)
                .fontWeight(FontWeight.Medium)
                .color(Colors.White),
            text = "Subscribe"
        )
    }
}