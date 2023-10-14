package com.hopcape.blog.data

import com.hopcape.blog.models.RandomJoke
import com.varabyte.kobweb.api.data.add
import com.varabyte.kobweb.api.init.InitApi
import com.varabyte.kobweb.api.init.InitApiContext

@InitApi
fun initJokesRepository(context: InitApiContext){
    context.data.add(RandomJokeRepositoryImpl())
}
class RandomJokeRepositoryImpl() : RandomJokeRepository{

    override suspend fun getRandomJoke(): RandomJoke {
       return RandomJoke(
           id = 12,
           joke = "Q:How are you today?...:Why will i tell you?"
       )
    }
}