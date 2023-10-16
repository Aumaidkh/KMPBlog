package com.hopcape.blog.data

import com.hopcape.blog.models.Post
import com.hopcape.blog.models.PostWithoutDetails
import com.hopcape.blog.models.User

interface MongoRepository {
    suspend fun checkUserExistence(user: User): User?

    suspend fun checkUserId(userId: String): Boolean

    suspend fun addPost(post: Post): Boolean

    suspend fun getMyPosts(skip: Int, author: String): List<PostWithoutDetails>
}