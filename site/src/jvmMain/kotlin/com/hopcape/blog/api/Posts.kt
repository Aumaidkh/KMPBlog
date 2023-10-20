package com.hopcape.blog.api

import com.hopcape.blog.data.MongoDB
import com.hopcape.blog.models.ApiListResponse
import com.hopcape.blog.models.Post
import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import com.varabyte.kobweb.api.data.getValue
import com.varabyte.kobweb.api.http.Request
import com.varabyte.kobweb.api.http.Response
import com.varabyte.kobweb.api.http.setBodyText
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.litote.kmongo.id.ObjectIdGenerator

@Api(routeOverride = "create-post")
suspend fun addPost(context: ApiContext){
    try {
        val post = context.req.getBody<Post>()
        post?._id = ObjectIdGenerator.newObjectId<String>().id.toHexString()
        context.res.setBody(
            data = post?.let { context.data.getValue<MongoDB>().addPost(it) } ?: false
        )
    } catch (e: Exception) {
        context.res.setException(e)
    }
}

@Api(routeOverride = "update-post")
suspend fun updatePost(context: ApiContext){
    try {
        val post = context.req.getBody<Post>()
        context.res.setBody(
            data = post?.let { context.data.getValue<MongoDB>().updatePost(it) } ?: false
        )
    } catch (e: Exception) {
        context.res.setException(e)
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
        context.res.setBody(posts)
    } catch (e: Exception) {
        context.res.setException(e)
    }
}

@Api(routeOverride = "main-posts")
suspend fun getMainPosts(context: ApiContext) {
    try {
        val posts = context
            .data
            .getValue<MongoDB>()
            .readMainPosts()
        context.res.setBody(posts)
    } catch (e: Exception) {
        context.res.setException(e)
    }
}

@Api(routeOverride = "latest-posts")
suspend fun getLatestPosts(context: ApiContext) {
    try {
        val skip = context.req.params["skip"]?.toInt() ?: 0
        val posts = context
            .data
            .getValue<MongoDB>()
            .readLatestPosts(skip)
        context.res.setBody(posts)
    } catch (e: Exception) {
        context.res.setException(e)
    }
}

@Api(routeOverride = "sponsored-posts")
suspend fun getSponsoredPosts(context: ApiContext) {
    try {
        val posts = context
            .data
            .getValue<MongoDB>()
            .readSponsoredPosts()
        context.res.setBody(posts)
    } catch (e: Exception) {
        context.res.setException(e)
    }
}

@Api(routeOverride = "popular-posts")
suspend fun getPopularPosts(context: ApiContext) {
    try {
        val skip = context.req.params["skip"]?.toInt() ?: 0
        val posts = context
            .data
            .getValue<MongoDB>()
            .readPopularPosts(skip)
        context.res.setBody(posts)
    } catch (e: Exception) {
        context.res.setException(e)
    }
}

@Api(routeOverride = "delete-posts")
suspend fun deletePosts(context: ApiContext){
    try {
        val postIds = context.req.getBody<List<String>>()
        context.res.setBody(
            postIds?.let { postIds ->
                context.data.getValue<MongoDB>().deleteSelectedPosts(postIds)
            } ?: false
        )
    } catch (e: Exception) {
        context.res.setException(e)
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
        context.res.setBody(result)
    } catch (e: Exception) {
        context.res.setException(e)
    }
}

@Api(routeOverride = "get-post")
suspend fun getPostById(context: ApiContext) {
    val postId = context.req.params["postId"]
    postId?.let {
        try {
            val post = context
                .data
                .getValue<MongoDB>()
                .getPostBy(
                    id = postId
                )
            context.res.setBody(post)
        } catch (e: Exception) {
            context.res.setException(e)
        }
    }
}

inline fun <reified T> Response.setBody(data: T){
    setBodyText(
        Json.encodeToString(data)
    )
}

inline fun <reified T> Request.getBody(): T? {
    return body?.decodeToString()?.let { Json.decodeFromString(it) }
}

fun Response.setException(e: Exception){
    setBody(e.message)
}