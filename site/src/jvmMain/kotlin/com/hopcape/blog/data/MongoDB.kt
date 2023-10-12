package com.hopcape.blog.data

import com.hopcape.blog.models.User
import com.hopcape.blog.utils.Constants.CONNECTION_STRING
import com.hopcape.blog.utils.Constants.DATABASE_NAME
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.varabyte.kobweb.api.data.add
import com.varabyte.kobweb.api.init.InitApi
import com.varabyte.kobweb.api.init.InitApiContext
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.reactive.awaitFirst
import org.litote.kmongo.and
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo
import org.litote.kmongo.reactivestreams.getCollection


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
}