package com.hopcape.androidapp.data

import com.hopcape.androidapp.RequestState
import kotlinx.coroutines.flow.Flow

interface IMongoRepository {
    fun configureTheRealm()
    fun readAllPosts(): Flow<RequestState<List<BlogPost>>>
}