package com.hopcape.blog.utils

import com.hopcape.blog.models.ApiListResponse
import com.hopcape.blog.models.ApiResponse
import com.hopcape.blog.models.Constants.AUTHOR_PARAM
import com.hopcape.blog.models.Constants.QUERY_PARAM
import com.hopcape.blog.models.Constants.QUERY_POST_ID
import com.hopcape.blog.models.Constants.SKIP_PARAM
import com.hopcape.blog.models.Post
import com.hopcape.blog.models.RandomJoke
import com.hopcape.blog.models.User
import com.hopcape.blog.utils.Constants.HUMOR_API_BASE_URL
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
        )?.decodeToString()
        result?.parseData<User>()
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
        )?.decodeToString()
        result.parseData()
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
        result?.decodeToString()?.parseData<RandomJoke>()
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
                    .get(HUMOR_API_BASE_URL)
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
                .get(HUMOR_API_BASE_URL)
                .decodeToString()
            localStorage["date"] = Date.now().toString()
            localStorage["joke"] = result
            Json.decodeFromString<RandomJoke>(result)
        } catch (e: Exception){
            RandomJoke(id = -1, joke = e.message.toString())
        }
    }
}

suspend fun addPost(post: Post): Boolean {
    return try {
        window.api.tryPost(
            apiPath = "create-post",
            body = Json.encodeToString(post).encodeToByteArray()
        )?.decodeToString().toBoolean()
    }catch (e: Exception){
        println("Exception: ${e.message}")
        false
    }
}

suspend fun updatePost(post: Post): Boolean {
    return try {
        window.api.tryPost(
            apiPath = "update-post",
            body = Json.encodeToString(post).encodeToByteArray()
        )?.decodeToString().toBoolean()
    }catch (e: Exception){
        println("Exception: ${e.message}")
        false
    }
}

suspend fun fetchPosts(skip: Int): ApiListResponse {
    return try {
        val response = window.api.tryGet(
            apiPath = "posts?$SKIP_PARAM=$skip&$AUTHOR_PARAM=${localStorage["username"]}"
        )?.decodeToString()
        ApiListResponse.Success(
            data = response.parseData()
        )
    }catch (e: Exception){
        e.printStackTrace()
        ApiListResponse
            .Error(e.message.toString())
    }
}

suspend fun fetchMainPosts(): ApiListResponse {
    return try {
        val response = window.api.tryGet(
            apiPath = "main-posts"
        )?.decodeToString()
        ApiListResponse.Success(
            data = response.parseData()
        )
    }catch (e: Exception){
        e.printStackTrace()
        ApiListResponse
            .Error(e.message.toString())
    }
}

suspend fun fetchPostBy(postId: String): ApiResponse {
    return try {
        val response = window.api.tryGet(
            apiPath = "get-post?$QUERY_POST_ID=$postId"
        )?.decodeToString()
        ApiResponse.Success(
            data = response.parseData()
        )
    }catch (e: Exception){
        e.printStackTrace()
        ApiResponse
            .Error(e.message.toString())
    }
}

suspend fun deletePosts(postIds: List<String>): Boolean {
    return try {
        window.api.tryPost(
            apiPath = "delete-posts",
            body = Json.encodeToString(postIds).encodeToByteArray()
        )?.decodeToString().toBoolean()
    }catch (e: Exception){
        false
    }
}

suspend fun searchPostsByTitle(
    query: String,
    skip: Int
): ApiListResponse {
    return try {
        val response = window.api.tryGet(
            apiPath = "search-posts?$QUERY_PARAM=$query&$SKIP_PARAM=$skip"
        )?.decodeToString()
        ApiListResponse.Success(
            data = response.parseData()
        )
    }catch (e: Exception){
        e.printStackTrace()
        ApiListResponse
            .Error(e.message.toString())
    }
}

inline fun <reified T> String?.parseData(): T {
    return Json.decodeFromString(this.toString())
}


