package com.hopcape.blog.data

import com.hopcape.blog.models.Post
import com.hopcape.blog.models.PostWithoutDetails
import com.hopcape.blog.models.User
import com.hopcape.blog.utils.Constants.CONNECTION_STRING
import com.hopcape.blog.utils.Constants.DATABASE_NAME
import com.hopcape.blog.utils.Constants.POSTS_PER_PAGE
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.varabyte.kobweb.api.data.add
import com.varabyte.kobweb.api.init.InitApi
import com.varabyte.kobweb.api.init.InitApiContext
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.litote.kmongo.and
import org.litote.kmongo.descending
import org.litote.kmongo.eq
import org.litote.kmongo.`in`
import org.litote.kmongo.regex


@InitApi
fun initMongoDB(context: InitApiContext){
    System.setProperty(
        "org.litote.mongo.test.mapping.service",
        "org.litote.kmongo.serialization.SerializationClassMappingTypeService"
    )
    context.data.add(MongoDB(context))
}

class MongoDB(private val context: InitApiContext): MongoRepository {
    private val client = MongoClient.create(CONNECTION_STRING)
    private val database = client.getDatabase(DATABASE_NAME)
    private val userCollection = database.getCollection<User>("user")
    private val postCollection = database.getCollection<Post>("post")
    override suspend fun checkUserExistence(user: User): User? {
        return try {
            userCollection.find(
                and(
                    User::username eq user.username,
                    User::password eq user.password
                )
            ).firstOrNull()
        }catch (e: Exception){
            context.logger.error(e.message.toString())
            null
        }
    }

    override suspend fun checkUserId(userId: String): Boolean {
        return try {
            val documentCount = userCollection.countDocuments(
                filter = User::id eq userId
            )
            documentCount > 0
        } catch (e: Exception){
            context.logger.error(e.message.toString())
            false
        }
    }

    override suspend fun addPost(post: Post): Boolean {
        return try {
            postCollection.insertOne(
                document = post
            ).wasAcknowledged()
        }catch (e: Exception){
            context.logger.error(e.message.toString())
            false
        }
    }

    override suspend fun getMyPosts(skip: Int, author: String): List<PostWithoutDetails> {
        return try {
            postCollection
                .withDocumentClass<PostWithoutDetails>()
                .find(PostWithoutDetails::author eq author)
                .sort(descending(PostWithoutDetails::date))
                .skip(skip)
                .limit(POSTS_PER_PAGE)
                .toList()

        }catch (e: Exception){
            context.logger.error(e.message.toString())
            emptyList()
        }
    }

    override suspend fun deleteSelectedPosts(postsIds: List<String>): Boolean {
        return try {
            postCollection
                .deleteMany(Post::_id `in` postsIds)
                .wasAcknowledged()

        }catch (e: Exception){
            context.logger.error(e.message.toString())
            false
        }
    }

    override suspend fun searchPostsByTitle(query: String, skip: Int): List<PostWithoutDetails> {
        val regexQuery = query.toRegex(RegexOption.IGNORE_CASE)
        return try {
            postCollection
                .withDocumentClass<PostWithoutDetails>()
                .find(PostWithoutDetails::title regex regexQuery)
                .sort(descending(PostWithoutDetails::date))
                .skip(skip)
                .limit(POSTS_PER_PAGE)
                .toList()

        }catch (e: Exception){
            context.logger.error(e.message.toString())
            emptyList()
        }
    }

    override suspend fun getPostBy(id: String): Post? {
        return try {
            postCollection
                .find(Post::_id eq id)
                .toList()
                .firstOrNull()

        }catch (e: Exception){
            context.logger.error(e.message.toString())
            null
        }
    }
}