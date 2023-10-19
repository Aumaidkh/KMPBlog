package com.hopcape.blog.pages.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.hopcape.blog.components.AdminPageLayout
import com.hopcape.blog.components.LinkPopup
import com.hopcape.blog.components.MessageBarPopup
import com.hopcape.blog.models.ApiResponse
import com.hopcape.blog.models.Category
import com.hopcape.blog.models.ControlStyle
import com.hopcape.blog.models.EditorControl
import com.hopcape.blog.models.Post
import com.hopcape.blog.models.Theme
import com.hopcape.blog.navigation.Screen
import com.hopcape.blog.styles.EditorKeyStyle
import com.hopcape.blog.styles.SwitchColorPalette
import com.hopcape.blog.models.Constants.QUERY_POST_ID
import com.hopcape.blog.models.Message
import com.hopcape.blog.utils.Constants.FONT_FAMILY
import com.hopcape.blog.utils.Constants.SIDE_PANEL_WIDTH
import com.hopcape.blog.utils.Id
import com.hopcape.blog.utils.addPost
import com.hopcape.blog.utils.applyControlStyle
import com.hopcape.blog.utils.applyStyle
import com.hopcape.blog.utils.fetchPostBy
import com.hopcape.blog.utils.getEditor
import com.hopcape.blog.utils.getSelectedText
import com.hopcape.blog.utils.isUserLoggedIn
import com.hopcape.blog.utils.noBorder
import com.hopcape.blog.utils.updatePost
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.Resize
import com.varabyte.kobweb.compose.css.ScrollBehavior
import com.varabyte.kobweb.compose.css.Visibility
import com.varabyte.kobweb.compose.file.loadDataUrlFromDisk
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.classNames
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.disabled
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxHeight
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onKeyDown
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.resize
import com.varabyte.kobweb.compose.ui.modifiers.scrollBehavior
import com.varabyte.kobweb.compose.ui.modifiers.visibility
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.forms.Switch
import com.varabyte.kobweb.silk.components.forms.SwitchSize
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.browser.document
import kotlinx.browser.localStorage
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.TextArea
import org.jetbrains.compose.web.dom.Ul
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLTextAreaElement
import org.w3c.dom.get
import kotlin.js.Date
import kotlin.math.ceil


data class CreatePageUiState(
    var id: String = "",
    var title: String = "",
    var subtitle: String = "",
    var content: String = "",
    var thumbnail: String = "",
    var thumbnailInputDisabled: Boolean = true,
    var category: Category = Category.Programming,
    var main: Boolean = false,
    var popular: Boolean = false,
    var sponsored: Boolean = false,
    var editorVisibility: Boolean = true,
    var errorMessage : String? = null,
    var linkPopup: Boolean = false,
    var imagePopup: Boolean = false
)

@Page
@Composable
fun CreatePage() {
    isUserLoggedIn {
        CreateScreen()
    }
}

@Composable
fun CreateScreen() {
    val breakpoint = rememberBreakpoint()
    val context = rememberPageContext()

    var uiState by remember {
        mutableStateOf(CreatePageUiState())
    }

    val hasPostId = remember(key1 = context.route) {
        context.route.params.containsKey(QUERY_POST_ID)
    }

    var message by remember {
        mutableStateOf<Message?>(null)
    }

    fun createDestinationRoute(): String {
        return if (hasPostId){
           Screen.AdminSuccess.postUpdated()
        } else {
            Screen.AdminSuccess.route
        }
    }


    LaunchedEffect(hasPostId) {
        if (hasPostId) {
            val postId = context.route.params[QUERY_POST_ID] ?: ""
            val response = fetchPostBy(postId)
            if (response is ApiResponse.Success) {
                val post = response.data

                uiState = uiState.copy(
                    id = post._id,
                    thumbnail = post.thumbnail,
                    title = post.title,
                    subtitle = post.subtitle,
                    category = post.category,
                    content = post.content,
                    sponsored = post.sponsored,
                    main = post.main,
                    popular = post.popular
                )
                (document.getElementById(Id.editor) as HTMLTextAreaElement)
                    .value = uiState.content
            }
        } else {
            (document.getElementById(Id.editor) as HTMLTextAreaElement)
                .value = ""
            uiState = CreatePageUiState()
        }
    }

    val scope = rememberCoroutineScope()

    AdminPageLayout {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .margin(topBottom = 50.px)
                .padding(left = if (breakpoint > Breakpoint.MD) SIDE_PANEL_WIDTH.px else 0.px),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .maxWidth(700.px),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                SimpleGrid(
                    numColumns = numColumns(base = 1, sm = 3)
                ) {
                    Row(
                        modifier = Modifier
                            .margin(
                                right = if (breakpoint < Breakpoint.SM) 0.px else 24.px,
                                bottom = if (breakpoint < Breakpoint.SM) 12.px else 0.px
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Switch(
                            modifier = Modifier.margin(right = 8.px),
                            checked = uiState.popular,
                            onCheckedChange = {
                                uiState = uiState.copy(
                                    popular = it
                                )
                            },
                            size = SwitchSize.LG,
                            colorScheme = SwitchColorPalette
                        )
                        SpanText(
                            modifier = Modifier
                                .fontFamily(FONT_FAMILY)
                                .fontSize(14.px)
                                .color(Theme.HalfBlack.rgb),
                            text = "Popular"
                        )
                    }
                    Row(
                        modifier = Modifier
                            .margin(
                                right = if (breakpoint < Breakpoint.SM) 0.px else 24.px,
                                bottom = if (breakpoint < Breakpoint.SM) 12.px else 0.px
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Switch(
                            modifier = Modifier.margin(right = 8.px),
                            checked = uiState.main,
                            onCheckedChange = {
                                uiState = uiState.copy(
                                    main = it
                                )
                            },
                            size = SwitchSize.LG,
                            colorScheme = SwitchColorPalette
                        )
                        SpanText(
                            modifier = Modifier
                                .fontFamily(FONT_FAMILY)
                                .fontSize(14.px)
                                .color(Theme.HalfBlack.rgb),
                            text = "Main"
                        )
                    }
                    Row(
                        modifier = Modifier
                            .margin(
                                right = if (breakpoint < Breakpoint.SM) 0.px else 24.px,
                                bottom = if (breakpoint < Breakpoint.SM) 12.px else 0.px
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Switch(
                            modifier = Modifier.margin(right = 8.px),
                            checked = uiState.sponsored,
                            onCheckedChange = {
                                uiState = uiState.copy(
                                    sponsored = it
                                )
                            },
                            size = SwitchSize.LG,
                            colorScheme = SwitchColorPalette
                        )
                        SpanText(
                            modifier = Modifier
                                .fontFamily(FONT_FAMILY)
                                .fontSize(14.px)
                                .color(Theme.HalfBlack.rgb),
                            text = "Sponsored"
                        )
                    }
                }
                Input(
                    attrs = Modifier
                        .id(Id.titleInput)
                        .fillMaxWidth()
                        .height(54.px)
                        .margin(topBottom = 12.px)
                        .padding(leftRight = 20.px)
                        .backgroundColor(Theme.LightGray.rgb)
                        .borderRadius(r = 8.px)
                        .noBorder()
                        .fontFamily(FONT_FAMILY)
                        .fontSize(16.px)
                        .toAttrs() {
                            attr("placeholder", "Title")
                            attr("value", uiState.title)
                        },
                    type = InputType.Text
                )
                Input(
                    attrs = Modifier
                        .id(Id.subtitleInput)
                        .fillMaxWidth()
                        .height(54.px)
                        .margin(bottom = 12.px)
                        .padding(leftRight = 20.px)
                        .backgroundColor(Theme.LightGray.rgb)
                        .borderRadius(r = 8.px)
                        .noBorder()
                        .fontFamily(FONT_FAMILY)
                        .fontSize(16.px)
                        .toAttrs() {
                            attr("placeholder", "Subtitle")
                            attr("value", uiState.subtitle)
                        },
                    type = InputType.Text
                )
                CategoryDropDown(
                    selectedCategory = uiState.category,
                    onCategorySelect = {
                        uiState = uiState.copy(
                            category = it
                        )
                    }
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .margin(topBottom = 12.px),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Switch(
                        modifier = Modifier.margin(right = 8.px),
                        checked = !uiState.thumbnailInputDisabled,
                        onCheckedChange = {
                           uiState = uiState.copy(
                               thumbnailInputDisabled = !it
                           )
                        },
                        size = SwitchSize.MD,
                        colorScheme = SwitchColorPalette
                    )
                    SpanText(
                        modifier = Modifier
                            .fontFamily(FONT_FAMILY)
                            .fontSize(14.px)
                            .color(Theme.HalfBlack.rgb),
                        text = "Paste an Image URL Instead"
                    )
                }

                ThumbnailUploader(
                    thumbnail = uiState.thumbnail,
                    thumbnailInputDisabled = uiState.thumbnailInputDisabled,
                    onThumbnailSelect = { fileName,file ->
                        uiState = uiState.copy(
                            thumbnail = fileName
                        )
                    }
                )

                EditorControls(
                    breakpoint = breakpoint,
                    editorVisibility = uiState.editorVisibility,
                    onEditorVisibilityChange = {
                        uiState = uiState.copy(
                            editorVisibility = !uiState.editorVisibility
                        )
                    },
                    onLinkClicked = {
                        uiState = uiState.copy(
                            linkPopup = true,
                            imagePopup = false
                        )
                    },
                    onImageClicked = {
                        uiState = uiState.copy(
                            imagePopup = true,
                            linkPopup = false
                        )
                    }
                )

                Editor(
                    editorVisibility = uiState.editorVisibility
                )

                CreateButton(
                    text = if (hasPostId) "Update" else "Create",
                    onClick = {

                        uiState =
                            uiState.copy(title = (document.getElementById(Id.titleInput) as HTMLInputElement).value)
                        uiState =
                            uiState.copy(subtitle = (document.getElementById(Id.subtitleInput) as HTMLInputElement).value)
                        uiState =
                            uiState.copy(content = (document.getElementById(Id.editor) as HTMLTextAreaElement).value)

                        /**
                         * Only if the get thumbnail by url is selected
                         * update the thumbnail from the input field*/
                        if(!uiState.thumbnailInputDisabled){
                            uiState =
                                uiState.copy(thumbnail = (document.getElementById(Id.thumbnailInput) as HTMLInputElement).value)

                        }

                        if (
                            uiState.title.isNotEmpty() &&
                            uiState.subtitle.isNotEmpty() &&
                            uiState.thumbnail.isNotEmpty() &&
                            uiState.content.isNotEmpty()) {

                            scope.launch {
                                val post = Post(
                                    _id = uiState.id,
                                    title = uiState.title,
                                    author = localStorage["username"].toString(),
                                    subtitle = uiState.subtitle,
                                    date = Date.now().toLong(),
                                    thumbnail = uiState.thumbnail,
                                    content = uiState.content,
                                    category = uiState.category,
                                    popular = uiState.popular,
                                    main = uiState.main,
                                    sponsored = uiState.sponsored
                                )
                                val result = createOrUpdatePost(
                                    create = !hasPostId,
                                    post = post
                                )
                                if (result){
                                    context
                                        .router
                                        .navigateTo(
                                            pathQueryAndFragment = createDestinationRoute()
                                        )
                                } else {
                                    message = Message.Error("Error adding post.")
                                }
                            }

                        } else {
                            scope.launch {
                                uiState = uiState.copy(
                                    errorMessage = "Please Fill All the fields"
                                )
                            }
                        }
                    }
                )
            }
        }
    }
    if (uiState.linkPopup){
        LinkPopup(
            editorControl = EditorControl.Link,
            onAddClicked = { href, title ->
                applyStyle(
                    ControlStyle.Link(
                        selectedText = getSelectedText(),
                        href = href,
                        title = title
                    )
                )
            },
            onDialogDismissed = {
                uiState = uiState.copy(
                    linkPopup = false
                )
            }
        )
    }

    if (uiState.imagePopup){
        LinkPopup(
            editorControl = EditorControl.Image,
            onAddClicked = { imageUrl, description ->
                applyStyle(
                    ControlStyle.Image(
                        selectedText = getSelectedText(),
                        imageLink = imageUrl,
                        desc = description
                    )
                )
            },
            onDialogDismissed = {
                uiState = uiState.copy(
                    imagePopup = false
                )
            }
        )
    }

    if (message != null){
        MessageBarPopup(
            message = message!!,
            onDialogDismissed = {
                message = null
            }
        )
    }

}

@Composable
private fun CreateButton(
    text: String = "Create",
    onClick: () -> Unit
) {
    Button(
        attrs = Modifier
            .fillMaxWidth()
            .height(54.px)
            .margin(top = 24.px)
            .padding(leftRight = 24.px)
            .borderRadius(r = 4.px)
            .noBorder()
            .backgroundColor(Theme.Primary.rgb)
            .color(Colors.White)
            .onClick {
                onClick()
            }
            .toAttrs()
    ) {
        SpanText(text)
    }
}

@Composable
fun CategoryDropDown(
    selectedCategory: Category,
    onCategorySelect: (Category) -> Unit
) {
    Box(
        modifier = Modifier
            .classNames("dropdown")
            .fillMaxWidth()
            .height(54.px)
            .backgroundColor(Theme.LightGray.rgb)
            .cursor(Cursor.Pointer)
            .attrsModifier {
                attr("data-bs-toggle","dropdown")
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(leftRight = 20.px),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            SpanText(
                modifier = Modifier
                    .fillMaxWidth()
                    .fontSize(16.px)
                    .fontFamily(FONT_FAMILY),
                text = selectedCategory.name
            )
            Box(
                modifier = Modifier
                    .classNames("dropdown-toggle")
            )
        }
        Ul(
            attrs = Modifier
                .fillMaxWidth()
                .classNames("dropdown-menu")
                .toAttrs()
        ) {
            Category.values().forEach { category ->
                Li{
                    A(
                        attrs = Modifier
                            .classNames("dropdown-item")
                            .color(Colors.Black)
                            .fontFamily(FONT_FAMILY)
                            .fontSize(16.px)
                            .onClick {
                                onCategorySelect(category)
                            }
                            .toAttrs()
                    ) {
                        Text(value = category.name)
                    }
                }
            }
        }
    }
}

@Composable
fun ThumbnailUploader(
    thumbnail: String,
    thumbnailInputDisabled: Boolean,
    onThumbnailSelect: (String,String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .margin(bottom = 20.px)
            .height(54.px)
    ) {
        Input(
            attrs = Modifier
                .id(Id.thumbnailInput)
                .margin(right = 12.px)
                .fillMaxSize()
                .padding(leftRight = 20.px)
                .backgroundColor(Theme.LightGray.rgb)
                .borderRadius(r=8.px)
                .fontSize(16.px)
                .fontFamily(FONT_FAMILY)
                .noBorder()
                .thenIf(
                    condition = thumbnailInputDisabled,
                    other = Modifier.disabled()
                )
                .toAttrs {
                      attr("placeholder","Thumbnail")
                      attr("value",thumbnail)
                },
            type = InputType.Text,
        )
        Button(
            attrs = Modifier
                .onClick {
                    document.loadDataUrlFromDisk(
                        accept = "image/png, image/jpeg",
                        onLoaded = {
                            onThumbnailSelect(filename,it)
                        }
                    )
                }
                .fillMaxHeight()
                .padding(leftRight = 24.px)
                .backgroundColor(if (thumbnailInputDisabled) Theme.Primary.rgb else Theme.Gray.rgb)
                .color(if (thumbnailInputDisabled) Colors.White else Theme.DarkGray.rgb)
                .noBorder()
                .fontFamily(FONT_FAMILY)
                .fontSize(14.px)
                .fontWeight(FontWeight.Medium)
                .borderRadius(r = 8.px)
                .thenIf(
                    condition = !thumbnailInputDisabled,
                    other = Modifier.disabled()
                )
                .toAttrs()
        ) {
            SpanText("Upload")
        }
    }
}

@Composable
fun EditorControls(
    breakpoint: Breakpoint,
    editorVisibility: Boolean,
    onLinkClicked: () -> Unit,
    onImageClicked: () -> Unit,
    onEditorVisibilityChange: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        SimpleGrid(
            modifier = Modifier
                .fillMaxWidth(),
            numColumns = numColumns(base = 1, sm = 2)
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .backgroundColor(Theme.LightGray.rgb)
                    .borderRadius(r = 4.px)
                    .height(54.px)
            ) {
                EditorControl.values().forEach { control ->
                    EditorControlView(
                        control = control,
                        onClick = {
                            applyControlStyle(
                                control = control,
                                onLinkClicked = onLinkClicked,
                                onImageClicked = onImageClicked
                            )
                        }
                    )
                }
            }
            Box(contentAlignment = Alignment.CenterEnd) {
                Button(
                    attrs = Modifier
                        .height(54.px)
                        .margin(topBottom = if (breakpoint < Breakpoint.SM) 12.px else 0.px)
                        .padding(leftRight = 24.px)
                        .borderRadius(r = 4.px)
                        .noBorder()
                        .backgroundColor(if (editorVisibility) Theme.LightGray.rgb else Theme.Primary.rgb)
                        .color(if (editorVisibility) Theme.DarkGray.rgb else Colors.White)
                        .onClick {
                            onEditorVisibilityChange()
                            document.getElementById(Id.editorPreview)?.innerHTML = getEditor().value
                            js("hljs.highlightAll()") as Unit
                        }
                        .thenIf(
                            condition = breakpoint < Breakpoint.SM,
                            other = Modifier
                                .fillMaxWidth()
                        )
                        .toAttrs()
                ) {
                    SpanText(
                        modifier = Modifier
                            .fontFamily(FONT_FAMILY)
                            .fontSize(14.px)
                            .fontWeight(FontWeight.Medium),
                        text = "Preview"
                    )
                }
            }
        }
        
    }
}

@Composable
fun EditorControlView(
    control: EditorControl,
    onClick: () -> Unit
) {
    Box(
        modifier = EditorKeyStyle
            .toModifier()
            .fillMaxHeight()
            .padding(leftRight = 12.px)
            .borderRadius(r = 4.px)
            .cursor(Cursor.Pointer)
            .onClick {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Image(
            src = control.icon
        )
    }
}

@Composable
fun Editor(
    editorVisibility: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        TextArea(
            attrs = Modifier
                .fillMaxWidth()
                .height(400.px)
                .maxHeight(400.px)
                .margin(top = 8.px)
                .padding(all = 20.px)
                .fontFamily(FONT_FAMILY)
                .fontSize(16.px)
                .backgroundColor(Theme.LightGray.rgb)
                .borderRadius(r = 4.px)
                .noBorder()
                .onKeyDown {
                    if (it.code == "Enter" && it.shiftKey){
                        applyStyle(
                            controlStyle = ControlStyle.Break(
                                selectedText = getSelectedText()
                            )
                        )
                    }
                }
                .id(Id.editor)
                .resize(
                    resize = Resize.None
                )
                .visibility(
                    if (editorVisibility) Visibility.Visible else Visibility.Hidden
                )
                .toAttrs{
                    attr("placeholder","Type here..")
                }
        )
        Div(
            attrs = Modifier
                .id(Id.editorPreview)
                .fillMaxWidth()
                .height(400.px)
                .maxHeight(400.px)
                .margin(top = 8.px)
                .padding(all = 20.px)
                .backgroundColor(Theme.LightGray.rgb)
                .borderRadius(r = 4.px)
                .noBorder()
                .visibility(if (editorVisibility) Visibility.Hidden else Visibility.Visible)
                .overflow(Overflow.Auto)
                .scrollBehavior(ScrollBehavior.Smooth)
                .toAttrs()
        ) {

        }
    }

}



private suspend fun createOrUpdatePost(create: Boolean, post: Post): Boolean {
    return if (create){
        addPost(post)
    } else {
        updatePost(post)
    }
}
