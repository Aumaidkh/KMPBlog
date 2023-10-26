package com.hopcape.androidapp.presentation.screens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hopcape.androidapp.RequestState
import com.hopcape.androidapp.data.APP_ID
import com.hopcape.androidapp.data.Post
import com.hopcape.androidapp.data.MongoSync
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel(){
    private val _posts: MutableState<RequestState<List<Post>>> = mutableStateOf(RequestState.Idle)
    val posts: State<RequestState<List<Post>>> = _posts

    private val _searchPosts: MutableState<RequestState<List<Post>>> = mutableStateOf(RequestState.Idle)
    val searchPosts: State<RequestState<List<Post>>> = _searchPosts
    init {
        viewModelScope.launch {
            App.Companion.create(APP_ID).login(
                credentials = Credentials.anonymous()
            )
            fetchAllPosts()
        }

    }

    private fun fetchAllPosts(){
        _posts.value = RequestState.Loading
        viewModelScope.launch {
            MongoSync.readAllPosts().collectLatest { _posts.value = it }
        }
    }

    fun searchPostsByTitle(query: String){
        _searchPosts.value = RequestState.Loading
        viewModelScope.launch {
            MongoSync.searchPostByTitle(query).collectLatest { _searchPosts.value = it }
        }
    }

    fun resetSearchedPosts(){
        _searchPosts.value = RequestState.Idle
    }

}