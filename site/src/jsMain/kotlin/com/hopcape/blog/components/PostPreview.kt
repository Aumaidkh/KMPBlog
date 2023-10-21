package com.hopcape.blog.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.hopcape.blog.models.PostWithoutDetails
import com.hopcape.blog.models.Theme
import com.hopcape.blog.navigation.Screen
import com.hopcape.blog.styles.PostPreviewStyle
import com.hopcape.blog.utils.Constants.FONT_FAMILY
import com.hopcape.blog.utils.parseDateString
import com.varabyte.kobweb.compose.css.AspectRatio
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.css.TextOverflow
import com.varabyte.kobweb.compose.css.TransitionProperty
import com.varabyte.kobweb.compose.css.Visibility
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.aspectRatio
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.minWidth
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.compose.ui.modifiers.textOverflow
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.visibility
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.CheckboxInput

@Composable
fun PostPreview(
    modifier: Modifier = Modifier,
    post: PostWithoutDetails,
    selectableMode: Boolean = false,
    darkTheme: Boolean = false,
    vertical: Boolean = true,
    thumbnailHeight: CSSSizeValue<CSSUnit.px>? = 280.px,
    onSelect: (String) -> Unit = {},
    onDeselect: (String) -> Unit = {},
    useColoredCategories: Boolean = true,
    onClick: (String) -> Unit = {}
) {

    var checked by remember(selectableMode) { mutableStateOf(false) }
    if (vertical){
        Column(
            modifier = PostPreviewStyle
                .toModifier()
                .then(modifier)
                .fillMaxWidth(if (darkTheme) 100.percent else 95.percent)
                .margin(bottom = 24.px)
                .padding(all = if (selectableMode) 10.px else 0.px)
                .borderRadius(r = 4.px)
                .transition(
                    CSSTransition(
                        property = TransitionProperty.All,
                        duration = 300.ms
                    )
                )
                .border(
                    width = if (selectableMode) 4.px else 0.px,
                    style = if (selectableMode) LineStyle.Solid else LineStyle.None,
                    color = if (checked) Theme.Primary.rgb else Theme.Gray.rgb
                )
                .onClick {
                    if (selectableMode){
                        checked = !checked
                        if (checked){
                            onSelect(post._id)
                        } else {
                            onDeselect(post._id)
                        }
                    } else {
                        onClick(post._id)
                    }
                }
                .cursor(Cursor.Pointer)
        ) {
            PostContent(
                post = post,
                darkTheme = darkTheme,
                selectableMode = selectableMode,
                checked = checked,
                vertical = vertical,
                thumbnailHeight = thumbnailHeight,
                useColoredCategories = useColoredCategories
            )
        }
    } else {
        Row(modifier = PostPreviewStyle
            .toModifier()
            .then(modifier)
            .onClick {  onClick(post._id) }
            .cursor(Cursor.Pointer)
        ) {
            PostContent(
                post = post,
                darkTheme = darkTheme,
                selectableMode = selectableMode,
                checked = checked,
                vertical = vertical,
                thumbnailHeight = thumbnailHeight,
                useFillMaxWidth = false,
                useColoredCategories = useColoredCategories
            )
        }
    }
}

@Composable
fun PostContent(
    post: PostWithoutDetails,
    darkTheme: Boolean,
    selectableMode: Boolean,
    checked: Boolean,
    vertical: Boolean,
    thumbnailHeight: CSSSizeValue<CSSUnit.px>?,
    useFillMaxWidth: Boolean = true,
    useColoredCategories: Boolean = true
) {

    Image(
        modifier = Modifier
            .margin(bottom = if (darkTheme) 20.px else 16.px)
            .thenIf(
                condition = useFillMaxWidth,
                other = Modifier
                    .fillMaxWidth()
            )
            .thenIf(
                condition = !useFillMaxWidth,
                other = Modifier
                    .minWidth(320.px)
            )
            .height(thumbnailHeight ?: 320.px)
            .objectFit(ObjectFit.Cover),
        src = post.thumbnail,
    )
    Column(
        modifier = Modifier
            .thenIf(
                condition = !vertical,
                other = Modifier
                    .margin(left = 24.px)
            )
    ) {
        SpanText(
            modifier = Modifier
                .fontFamily(FONT_FAMILY)
                .fontSize(10.px)
                .color(if (darkTheme) Theme.HalfWhite.rgb else Theme.HalfBlack.rgb),
            text = post.date.parseDateString()
        )

        SpanText(
            modifier = Modifier
                .margin(bottom = 8.px)
                .fontFamily(FONT_FAMILY)
                .fontSize(20.px)
                .fontWeight(FontWeight.Bold)
                .textOverflow(TextOverflow.Ellipsis)
                .overflow(Overflow.Hidden)
                .color(if (darkTheme) Colors.White else Colors.Black)
                .styleModifier {
                    property("display", "-webkit-box")
                    property("-webkit-line-clamp", "2")
                    property("line-clamp", "2")
                    property("-webkit-box-orient", "vertical")
                },
            text = post.title
        )
        SpanText(
            modifier = Modifier
                .margin(bottom = 10.px)
                .fontFamily(FONT_FAMILY)
                .fontSize(14.px)
                .textOverflow(TextOverflow.Ellipsis)
                .overflow(Overflow.Hidden)
                .color(if (darkTheme) Colors.White else Colors.Black)
                .styleModifier {
                    property("display", "-webkit-box")
                    property("-webkit-line-clamp", "3")
                    property("line-clamp", "3")
                    property("-webkit-box-orient", "vertical")
                },
            text = post.subtitle
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CategoryChip(
                category = post.category,
                coloredCategories = useColoredCategories
            )
            if (selectableMode){
                CheckboxInput(
                    attrs = Modifier
                        .size(20.px)
                        .toAttrs(),
                    checked = checked
                )
            }
        }
    }
}

