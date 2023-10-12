package com.hopcape.blog.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonProperty
import org.litote.kmongo.id.ObjectIdGenerator

@Serializable
actual data class User(
    @SerialName(value = "_id")
    @BsonProperty("_id")
    actual val id: String = ObjectIdGenerator.newObjectId<String>().id.toHexString(),
    @SerialName(value = "username")
    actual val username: String = "",
    @SerialName(value = "password")
    actual val password: String = ""
)
