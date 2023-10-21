package com.hopcape.blog.models

import kotlinx.serialization.Serializable

@Serializable
data class NewsLetter(
    val email: String
)
