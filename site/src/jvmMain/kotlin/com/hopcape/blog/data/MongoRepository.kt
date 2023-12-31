package com.hopcape.blog.data

import com.hopcape.blog.models.Category
import com.hopcape.blog.models.NewsLetter
import com.hopcape.blog.models.Post
import com.hopcape.blog.models.PostWithoutDetails
import com.hopcape.blog.models.User

interface MongoRepository {
    suspend fun checkUserExistence(user: User): User?

    suspend fun checkUserId(userId: String): Boolean

    suspend fun addPost(post: Post): Boolean

    suspend fun getMyPosts(skip: Int, author: String): List<PostWithoutDetails>

    suspend fun deleteSelectedPosts(postsIds: List<String>): Boolean

    suspend fun searchPostsByTitle(query: String,skip: Int): List<PostWithoutDetails>

    suspend fun getPostBy(id: String): Post?

    suspend fun updatePost(post: Post): Boolean

    suspend fun readMainPosts(): List<PostWithoutDetails>
    suspend fun readLatestPosts(skip: Int): List<PostWithoutDetails>
    suspend fun readSponsoredPosts(): List<PostWithoutDetails>
    suspend fun readPopularPosts(skip: Int): List<PostWithoutDetails>

    suspend fun searchPostsByCategory(category: Category, skip: Int): List<PostWithoutDetails>

    /**
     * News Letter*/
    suspend fun subscribe(newsLetter: NewsLetter): String
}