package com.hopcape.androidapp.data

import com.hopcape.androidapp.RequestState
import io.realm.kotlin.Realm
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

const val APP_ID = ""
object MongoSync : IMongoRepository {
    private val app = App.create(APP_ID)
    private val user = app.currentUser
    private lateinit var realm: Realm

    init {
        configureTheRealm()
    }

    override fun configureTheRealm() {
        if (user != null) {
            val config = SyncConfiguration.Builder(user, setOf(BlogPost::class))
                .initialSubscriptions {
                    add(
                        query = it.query(BlogPost::class),
                        name = "Blog Posts"
                    )
                }
                .log(LogLevel.ALL)
                .build()
            realm = Realm.open(config)
        }
    }

    override fun readAllPosts(): Flow<RequestState<List<BlogPost>>> {
        return if (user != null) {
            try {
                realm.query(BlogPost::class)
                    .asFlow()
                    .map { result ->
                        RequestState.Success(data = result.list)
                    }
            } catch (e: Exception) {
                flow { emit(RequestState.Error(Exception(e.message))) }
            }
        } else {
            flow { emit(RequestState.Error(Exception("User not authenticated."))) }
        }
    }

}