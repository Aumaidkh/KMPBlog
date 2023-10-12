package com.hopcape.blog.utils

import com.hopcape.blog.models.User
import com.varabyte.kobweb.browser.api
import kotlinx.browser.window
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

suspend fun checkUserExistence(user: User): User?{
    return try {
        val result = window.api.tryPost(
            apiPath = "usercheck",
            body = Json.encodeToString(user).encodeToByteArray()
        )
        result?.decodeToString()?.let { Json.decodeFromString(it) }
    } catch (e: Exception){
        println(e.message)
        null
    }
}

suspend fun checkUserId(userId: String): Boolean {
    return try {
        val result = window.api.tryPost(
            apiPath = "checkuserid",
            body = Json.encodeToString(userId).encodeToByteArray()
        )
        result?.decodeToString()?.let { Json.decodeFromString(it) } ?: false
    } catch (e: Exception){
        println(e.message)
        false
    }
}