package com.hopcape.blog.data

import com.hopcape.blog.models.RandomJoke

interface RandomJokeRepository {
    suspend fun getRandomJoke(): RandomJoke
}