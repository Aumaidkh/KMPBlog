package com.hopcape.blog.utils

object Constants {
    const val FONT_FAMILY = "Poppins"
    const val SIDE_PANEL_WIDTH = 250
    const val SECTION_PAGE_WIDTH = 1920
    const val HEADER_HEIGHT = 100
    const val COLLAPSED_PANEL_HEIGHT = 100
    const val HUMOR_API_BASE_URL = "https://api.humorapi.com/jokes/random?api-key=bb52c9feefa24cf890d8b7dc56f1f2f6&max-length=180"

}

object Resource {
    object Image {
        const val logo = "/logo.svg"
        const val blogLogo = "logo.svg"
        const val searchLogo = "/logo.svg"
        const val joke = "/joke.png"
    }

    object Icon {
        const val bold = "/type-bold.svg"
        const val italic = "/type-italic.svg"
        const val link = "/link-45deg.svg"
        const val title = "/fonts.svg"
        const val subtitle = "/fonts.svg"
        const val quote = "/quote.svg"
        const val code = "/code-slash.svg"
        const val image = "/card-image.svg"
        const val success = "/check-circle.svg"
    }

    object PathIcons {
        const val home = "M8.707 1.5a1 1 0 0 0-1.414 0L.646 8.146a.5.5 0 0 0 .708.708L2 8.207V13.5A1.5 1.5 0 0 0 3.5 15h9a1.5 1.5 0 0 0 1.5-1.5V8.207l.646.647a.5.5 0 0 0 .708-.708L13 5.793V2.5a.5.5 0 0 0-.5-.5h-1a.5.5 0 0 0-.5.5v1.293L8.707 1.5ZM13 7.207V13.5a.5.5 0 0 1-.5.5h-9a.5.5 0 0 1-.5-.5V7.207l5-5 5 5Z"
        const val create = "M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14zm0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16zM8 4a.5.5 0 0 1 .5.5v3h3a.5.5 0 0 1 0 1h-3v3a.5.5 0 0 1-1 0v-3h-3a.5.5 0 0 1 0-1h3v-3A.5.5 0 0 1 8 4z"
        const val posts = "M3 0h10a2 2 0 0 1 2 2v12a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2v-1h1v1a1 1 0 0 0 1 1h10a1 1 0 0 0 1-1V2a1 1 0 0 0-1-1H3a1 1 0 0 0-1 1v1H1V2a2 2 0 0 1 2-2zM1 5v-.5a.5.5 0 0 1 1 0V5h.5a.5.5 0 0 1 0 1h-2a.5.5 0 0 1 0-1H1zm0 3v-.5a.5.5 0 0 1 1 0V8h.5a.5.5 0 0 1 0 1h-2a.5.5 0 0 1 0-1H1zm0 3v-.5a.5.5 0 0 1 1 0v.5h.5a.5.5 0 0 1 0 1h-2a.5.5 0 0 1 0-1H1z"
        const val logout = "M10 12.5a.5.5 0 0 1-.5.5h-8a.5.5 0 0 1-.5-.5v-9a.5.5 0 0 1 .5-.5h8a.5.5 0 0 1 .5.5v2a.5.5 0 0 0 1 0v-2A1.5 1.5 0 0 0 9.5 2h-8A1.5 1.5 0 0 0 0 3.5v9A1.5 1.5 0 0 0 1.5 14h8a1.5 1.5 0 0 0 1.5-1.5v-2a.5.5 0 0 0-1 0v2zM15.854 8.354a.5.5 0 0 0 0-.708l-3-3a.5.5 0 0 0-.708.708L14.293 7.5H5.5a.5.5 0 0 0 0 1h8.793l-2.147 2.146a.5.5 0 0 0 .708.708l3-3z"
    }
}

object Id {
    // Login Section Ids
    const val usernameInput = "usernameInput"
    const val passwordInput = "passwordInput"
    const val svgParent = "svgParent"
    const val vectorIcon = "vectorIcon"

    // Create Screen Ids
    const val navigationText = "navigationText"
    const val editor = "editor"
    const val editorPreview = "editorPreview"
    const val titleInput = "titleInput"
    const val subtitleInput = "subtitleInput"
    const val thumbnailInput = "thumbnailInput"
    const val contentInput = "contentInput"
    const val linkHrefInput = "linkHrefInput"
    const val linkTitleInput = "linkTitleInput"
    const val searchInput = "searchInput"

    // Website Home Screen
    const val emailInput = "emailInput"
}