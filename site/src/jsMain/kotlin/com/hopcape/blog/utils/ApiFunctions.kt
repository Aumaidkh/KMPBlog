package com.hopcape.blog.utils

import com.hopcape.blog.models.RandomJoke
import com.hopcape.blog.models.User
import com.varabyte.kobweb.browser.api
import com.varabyte.kobweb.compose.http.http
import kotlinx.browser.localStorage
import kotlinx.browser.window
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.w3c.dom.get
import org.w3c.dom.set
import kotlin.js.Date

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

suspend fun getRandomJoke(): RandomJoke?{
    return try {
        val result = window.api.tryGet(
            apiPath = "random-joke"
        )
        result?.decodeToString()?.let { Json.decodeFromString(it) }
    }catch (e: Exception){
        null
    }
}

suspend fun fetchRandomJoke(): RandomJoke?{
    val date = localStorage["date"]
    return if (date != null){
        val difference = (Date.now() - date.toDouble())
        val dayHasPassed = difference >= 86400000
        if (dayHasPassed){
            try {
                val result = window
                    .http
                    .get(Constants.HUMOR_API_BASE_URL)
                    .decodeToString()
                localStorage["date"] = Date.now().toString()
                localStorage["joke"] = result
                Json.decodeFromString<RandomJoke>(result)
            } catch (e: Exception){
                println(e.message)
                RandomJoke(id = -1, joke = e.message.toString())
            }
        } else {
            try {
                localStorage["joke"]?.let { Json.decodeFromString(it) }
            } catch (e: Exception){
                RandomJoke(id = -1, joke = e.message.toString())

            }
        }
    } else {
        try {
            val result = window
                .http
                .get(Constants.HUMOR_API_BASE_URL)
                .decodeToString()
            localStorage["date"] = Date.now().toString()
            localStorage["joke"] = result
            Json.decodeFromString<RandomJoke>(result)
        } catch (e: Exception){
            RandomJoke(id = -1, joke = e.message.toString())
        }
    }
}