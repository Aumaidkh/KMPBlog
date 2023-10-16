package com.hopcape.blog.models


expect class Post {
    var _id: String
    val author: String
    val date: Long
    val title: String
    val subtitle: String
    val thumbnail: String
    val content: String
    val category: Category
    val popular: Boolean
    val main: Boolean
    val sponsored: Boolean
}

expect class PostWithoutDetails {
    val _id: String
    val author: String
    val date: Long
    val title: String
    val subtitle: String
    val thumbnail: String
    val category: Category
}