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

@Api(routeOverride = "delete-posts")
suspend fun deletePosts(context: ApiContext){
    try {
        val postIds = context.req.body?.decodeToString()?.let {
            Json.decodeFromString<List<String>>(it)
        }
        context.res.setBodyText(
            postIds?.let { postIds ->
                context.data.getValue<MongoDB>().deleteSelectedPosts(postIds).toString()
            } ?: false.toString()
        )
    } catch (e: Exception) {
        context.res.setBodyText(
            text = Json.encodeToString(e.message)
        )
    }
}

@Api(routeOverride = "search-posts")
suspend fun searchPostsByTitle(context: ApiContext){
    try {
        val query = context.req.params["query"] ?: ""
        val skip = context.req.params["skip"]?.toInt() ?: 0
        val result = context
            .data
            .getValue<MongoDB>()
            .searchPostsByTitle(query=query,skip=skip)
            .toList()
        context.res.setBodyText(
            Json.encodeToString(result)
        )
    } catch (e: Exception) {
        context.res.setBodyText(
            text = Json.encodeToString(e.message)
        )
    }
}