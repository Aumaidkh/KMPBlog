package com.hopcape.blog.api

import com.hopcape.blog.data.RandomJokeRepository
import com.hopcape.blog.data.RandomJokeRepositoryImpl
import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import com.varabyte.kobweb.api.http.setBodyText
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Api(routeOverride = "random-joke")
suspend fun getRandomJoke(apiContext: ApiContext) {
    val repo: RandomJokeRepository = RandomJokeRepositoryImpl()
     try {
        val randomJoke = repo.getRandomJoke()
         apiContext.res.setBodyText(Json.encodeToString(randomJoke))
    } catch (e: Exception){
         apiContext.res.setBodyText(Json.encodeToString(MyException(e.message.toString())))
    }
}