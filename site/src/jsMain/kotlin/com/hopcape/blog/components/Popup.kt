package com.hopcape.blog.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.hopcape.blog.models.EditorControl
import com.hopcape.blog.models.Message
import com.hopcape.blog.models.Theme
import com.hopcape.blog.utils.Constants.FONT_FAMILY
import com.hopcape.blog.utils.Id
import com.hopcape.blog.utils.isMobile
import com.hopcape.blog.utils.noBorder
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.css.TransitionProperty
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.classNames
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
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
import com.varabyte.kobweb.compose.ui.modifiers.translate
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.text.SpanText
import kotlinx.browser.document
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
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
    message: Message,
    breakpoint: Breakpoint,
    onDialogDismissed: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var percentage by remember {
        mutableStateOf(0)
    }
    scope.launch {
        (0 until 100) .forEach {
            delay(5)
            percentage = it
        }
    }
    LaunchedEffect(key1 = Unit){
        delay(2000)
        onDialogDismissed()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .position(Position.Fixed)
            .zIndex(19)
            .backgroundColor(Theme.HalfBlack.rgb),
        contentAlignment = Alignment.TopEnd
    ) {
        Row(
            modifier = Modifier
                .margin(
                    top = 24.px,
                    right = if (breakpoint.isMobile()) 40.px else 120.px
                )
                .borderRadius(r = 4.px)
                .noBorder()
                .fillMaxWidth(50.percent)
                .height(if (breakpoint.isMobile()) 60.px else 120.px)
                .padding(
                    leftRight = if (breakpoint.isMobile()) 10.px else 20.px,
                    topBottom = if (breakpoint.isMobile()) 20.px else 20.px
                )
                .backgroundColor(Colors.White)
                .transition(CSSTransition(
                    property = TransitionProperty.All,
                    duration = 300.ms
                ))
                .translate(tx = 10.percent)
            ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Box(
                modifier = Modifier
                    .margin(right = 24.px)
                    .fillMaxHeight()
                    .borderRadius(r = 1.px)
                    .backgroundColor(Colors.WhiteSmoke)
                    .width(if (breakpoint.isMobile()) 5.px else 10.px)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(right = 24.px),
                horizontalAlignment = Alignment.Start
            ) {
                SpanText(
                    modifier = Modifier
                        .fontFamily(FONT_FAMILY)
                        .fontSize(if (breakpoint.isMobile()) 14.px else 24.px)
                        .fontWeight(FontWeight.Medium)
                        .color(message.getColor()),
                    text = message::class.simpleName.toString()
                )
                SpanText(
                    modifier = Modifier
                        .margin(bottom = if (breakpoint.isMobile()) 5.px else 24.px)
                        .fontFamily(FONT_FAMILY)
                        .fontSize(if (breakpoint.isMobile()) 11.px else 16.px)
                        .fontWeight(if (breakpoint.isMobile()) FontWeight.Normal else FontWeight.Medium)
                        .color(Colors.Black),
                    text = message.data
                )

                Div(
                    attrs = Modifier
                        .fillMaxWidth()
                        .height(if (breakpoint.isMobile()) 5.px else 10.px)
                        .classNames("progress")
                        .toAttrs{
                            attr("role","progressbar")
                            attr("aria-label","Example")
                            attr("aria-valuenow","20")
                            attr("aria-valuemin","0")
                            attr("aria-valuemax","100")
                        }
                ) {
                    Div(
                        attrs = Modifier
                            .classNames("progress-bar",message.getProgressBarTypeClass())
                            .transition(
                                CSSTransition(
                                    property = TransitionProperty.All,
                                    duration = 300.ms
                                )
                            )
                            .styleModifier {
                                property("width","$percentage%")
                            }
                            .toAttrs()
                    )
                }

            }

        }
    }
}




