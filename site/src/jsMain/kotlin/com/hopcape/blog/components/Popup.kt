package com.hopcape.blog.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.hopcape.blog.models.EditorControl
import com.hopcape.blog.models.Theme
import com.hopcape.blog.utils.Constants.FONT_FAMILY
import com.hopcape.blog.utils.Id
import com.hopcape.blog.utils.noBorder
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.css.TransitionProperty
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.boxShadow
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.text.SpanText
import kotlinx.browser.document
import kotlinx.coroutines.delay
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Input
import org.w3c.dom.HTMLInputElement

@Composable
fun MessagePopup(
    message: String,
    onDialogDismissed: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .position(Position.Fixed)
            .zIndex(19),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .backgroundColor(Theme.HalfBlack.rgb)
                .onClick {
                    onDialogDismissed()
                },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .padding(24.px)
                    .backgroundColor(Colors.White)
            ) {
                SpanText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .textAlign(TextAlign.Center)
                        .fontFamily(FONT_FAMILY)
                        .fontSize(16.px),
                    text = message
                )
            }
        }
    }
}

@Composable
fun LinkPopup(
    editorControl: EditorControl,
    onAddClicked: (String, String) -> Unit,
    onDialogDismissed: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .position(Position.Fixed)
            .zIndex(19),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .backgroundColor(Theme.HalfBlack.rgb)
                .onClick {
                    //onDialogDismissed()
                },
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .width(500.px)
                    .padding(all = 24.px)
                    .backgroundColor(Colors.White)
                    .borderRadius(r = 4.px)
            ) {
                Input(
                    attrs = Modifier
                        .id(Id.linkHrefInput)
                        .fillMaxWidth()
                        .height(54.px)
                        .padding(left = 20.px)
                        .margin(bottom = 12.px)
                        .fontFamily(FONT_FAMILY)
                        .fontSize(14.px)
                        .borderRadius(r = 4.px)
                        .backgroundColor(Theme.LightGray.rgb)
                        .noBorder()
                        .toAttrs{
                            attr("placeholder",if (editorControl == EditorControl.Link) "Href" else "Image URL")
                        },
                    type = InputType.Text
                )

                Input(
                    attrs = Modifier
                        .id(Id.linkTitleInput)
                        .fillMaxWidth()
                        .margin(bottom = 20.px)
                        .height(54.px)
                        .padding(left = 20.px)
                        .fontFamily(FONT_FAMILY)
                        .fontSize(14.px)
                        .borderRadius(r = 4.px)
                        .backgroundColor(Theme.LightGray.rgb)
                        .noBorder()
                        .toAttrs{
                            attr("placeholder",if (editorControl == EditorControl.Link) "Title" else "Description")
                        },
                    type = InputType.Text
                )

                Button(
                    attrs = Modifier
                        .onClick {
                            val href = (document.getElementById(Id.linkHrefInput) as HTMLInputElement).value
                            val title = (document.getElementById(Id.linkTitleInput) as HTMLInputElement).value
                            onAddClicked(href,title)
                            onDialogDismissed()
                        }
                        .fillMaxWidth()
                        .height(54.px)
                        .backgroundColor(Theme.Primary.rgb)
                        .borderRadius(r = 4.px)
                        .noBorder()
                        .fontSize(14.px)
                        .fontFamily(FONT_FAMILY)
                        .toAttrs()
                ){
                    SpanText(
                        text = "Add"
                    )
                }
            }
        }
    }
}

@Composable
fun MessageBarPopup(
    message: String,
    onDialogDismissed: () -> Unit
) {
    LaunchedEffect(key1 = Unit){
        delay(2000)
        onDialogDismissed()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .backgroundColor(Theme.HalfBlack.rgb)
            .transition(CSSTransition(
                property = TransitionProperty.All,
                duration = 300.ms
            )),
        contentAlignment = Alignment.TopEnd
    ) {
        Box(
            modifier = Modifier
                .margin(all = 16.px)
                .borderRadius(r = 4.px)
                .boxShadow(offsetX = 10.px, offsetY = 10.px, blurRadius = 10.px, spreadRadius = 10.px, color = Theme.HalfBlack.rgb)
                .noBorder()
                .fillMaxWidth(50.percent)
                .padding(leftRight = 50.px, topBottom = 20.px)
                .backgroundColor(Colors.White)
            ,
            contentAlignment = Alignment.Center
        ) {
            SpanText(
                modifier = Modifier
                    .fontFamily(FONT_FAMILY)
                    .fontSize(20.px)
                    .fontWeight(FontWeight.Medium)
                    .color(Colors.Black),
                text = message
            )
        }
    }
}


