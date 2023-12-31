package com.hopcape.blog.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
actual data class User (
    @SerialName(value = "_id")
    actual val id: String = "",
    actual val username: String,
    actual val password: String,
)