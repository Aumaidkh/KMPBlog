package com.hopcape.blog.models

import kotlinx.serialization.Serializable

@Serializable
actual data class RandomJoke(
    actual val id: Int,
    actual val joke: String
)
