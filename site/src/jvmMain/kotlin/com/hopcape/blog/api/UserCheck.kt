package com.hopcape.blog.api

import com.hopcape.blog.data.MongoDB
import com.hopcape.blog.models.User
import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import com.varabyte.kobweb.api.data.getValue
import com.varabyte.kobweb.api.http.setBodyText
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

@Api(routeOverride = "usercheck")
suspend fun userCheck(context: ApiContext){
    try {
        // Getting and Constructing User Request
        val userRequest = context.req.body?.decodeToString()?.let {
            Json.decodeFromString<User>(it)
        }

        val user = userRequest?.let {
            context.data.getValue<MongoDB>().checkUserExistence(
                user = User(
                    username = it.username,
                    password = hashPassword(it.password)
                )
            )
        }

        if (user != null){
            context.res.setBodyText(
                Json.encodeToString(user)
            )
        } else {
            context.res.setBodyText(
                Json.encodeToString(MyException("User doesn't Exist"))
            )
        }
    }catch (e: Exception){
        context.res.setBodyText(
            Json.encodeToString(MyException(e.message.toString()))
        )
    }
}

@Api(routeOverride = "checkuserid")
suspend fun checkUserId(context: ApiContext){
    try {
        val idRequest = context.req.body?.decodeToString()?.let {
            Json.decodeFromString<String>(it)
        }

        val result = idRequest?.let {
            context.data.getValue<MongoDB>().checkUserId(it)
        }

        context.res.setBodyText(
            Json.encodeToString(result?:false)
        )
    } catch (e: Exception){
        context.res.setBodyText(
            Json.encodeToString(false)
        )
    }
}

private fun hashPassword(password: String): String {
    val messageDigest = MessageDigest.getInstance("SHA-256")
    val hashBytes = messageDigest.digest(password.toByteArray(StandardCharsets.UTF_8))
    val hexString = StringBuffer()

    for (byte in hashBytes){
        hexString.append(String.format("%02x",byte))
    }

    return hexString.toString()
}