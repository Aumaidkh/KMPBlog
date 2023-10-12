package com.hopcape.blog.navigation

sealed class Screen(val route: String){
    data object AdminHome: Screen(route = "/admin/")
    data object AdminLogin: Screen(route = "/admin/login")
    data object AdminCreate: Screen(route = "/admin/create")
    data object AdminPosts: Screen(route = "/admin/posts")
}
