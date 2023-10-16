package com.hopcape.blog.models

expect sealed class ApiListResponse {
    object Idle
    class Success
    class Error
}