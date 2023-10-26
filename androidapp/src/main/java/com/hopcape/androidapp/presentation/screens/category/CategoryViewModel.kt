package com.hopcape.androidapp.presentation.screens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hopcape.androidapp.RequestState
import com.hopcape.androidapp.data.Category
import com.hopcape.androidapp.data.MongoSync
import com.hopcape.androidapp.data.Post
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CategoryViewModel(
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _posts: MutableState<RequestState<List<Post>>> = mutableStateOf(RequestState.Idle)
    val posts: State<RequestState<List<Post>>> = _posts

    init {
        val category = savedStateHandle.get<String>("category")
        category?.let {
            fetchPostsByCategory(Category.valueOf(it))
        }
    }

    private fun fetchPostsByCategory(category: Category){
        _posts.value = RequestState.Loading
        viewModelScope.launch {
            MongoSync
                .searchPostsBy(category)
                .collectLatest { _posts.value = it }
        }
    }
}
