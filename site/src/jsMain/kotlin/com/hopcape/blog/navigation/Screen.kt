package com.hopcape.blog.navigation

import com.hopcape.blog.utils.Constants.QUERY_PARAM
import com.hopcape.blog.utils.Constants.QUERY_POST_ID

sealed class Screen(val route: String){
    data object AdminHome: Screen(route = "/admin/")
    data object AdminLogin: Screen(route = "/admin/login")
    data object AdminCreate: Screen(route = "/admin/create") {
        fun passPostId(id: String): String {
            return "/admin/create?$QUERY_POST_ID=$id"
        }
    }
    data object AdminPosts: Screen(route = "/admin/posts"){
        fun searchByTitle(query: String): String {
            return "/admin/posts?$QUERY_PARAM=$query"
        }
    }
    data object AdminSuccess: Screen(route = "/admin/success")
}
