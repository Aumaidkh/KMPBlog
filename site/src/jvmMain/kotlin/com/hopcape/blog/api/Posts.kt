package com.hopcape.blog.api

import com.hopcape.blog.data.MongoDB
import com.hopcape.blog.models.ApiListResponse
import com.hopcape.blog.models.Post
import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import com.varabyte.kobweb.api.data.getValue
import com.varabyte.kobweb.api.http.setBodyText
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.litote.kmongo.id.ObjectIdGenerator

@Api(routeOverride = "create-post")
suspend fun addPost(context: ApiContext){
    try {
        val post = context.req.body?.decodeToString()?.let {
            Json.decodeFromString<Post>(it)
        }
        post?._id = ObjectIdGenerator.newObjectId<String>().id.toHexString()
        context.res.setBodyText(
            post?.let { newPost ->
                context.data.getValue<MongoDB>().addPost(newPost).toString()
            } ?: false.toString()
        )
    } catch (e: Exception) {
        context.res.setBodyText(
            text = Json.encodeToString(e.message)
        )
    }
}

@Api(routeOverride = "posts")
suspend fun getPosts(context: ApiContext) {
    try {
        val skip = context.req.params["skip"]?.toInt() ?: 0
        val author = context.req.params["author"] ?: ""

        val posts = context
            .data
            .getValue<MongoDB>()
            .getMyPosts(
                skip = skip,
                author = author
            )
        context.res.setBodyText(
            Json.encodeToString(posts)
        )
    } catch (e: Exception) {
        context.res.setBodyText(
            text = Json.encodeToString(e.message)
        )
    }
}