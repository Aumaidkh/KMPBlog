package com.hopcape.blog.api

import com.hopcape.blog.data.MongoDB
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
        val newPost = post?.copy(
            id = ObjectIdGenerator.newObjectId<String>().id.toHexString()
        )
        context.res.setBodyText(
            newPost?.let {
                context.data.getValue<MongoDB>().addPost(it).toString()
            } ?: false.toString()
        )
    }catch (e: Exception){
        context.res.setBodyText(
            text = Json.encodeToString(e.message)
        )
    }
}