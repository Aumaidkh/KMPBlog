package com.hopcape.blog.models

import com.hopcape.blog.utils.Resource

enum class EditorControl(
    val icon: String
) {
    Bold(Resource.Icon.bold),
    Italic(Resource.Icon.italic),
    Link(Resource.Icon.link),
    Title(Resource.Icon.title),
    Subtitle(Resource.Icon.subtitle),
    Quote(Resource.Icon.quote),
    Code(Resource.Icon.code),
    Image(Resource.Icon.image),
}