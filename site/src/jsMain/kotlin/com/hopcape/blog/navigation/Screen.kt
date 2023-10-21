package com.hopcape.blog.navigation

import com.hopcape.blog.models.Category
import com.hopcape.blog.models.Constants.CATEGORY_PARAM
import com.hopcape.blog.models.Constants.POST_UPDATED_PARAM
import com.hopcape.blog.models.Constants.QUERY_PARAM
import com.hopcape.blog.models.Constants.QUERY_POST_ID

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
    data object AdminSuccess: Screen(route = "/admin/success"){
        fun postUpdated() = "/admin/success?$POST_UPDATED_PARAM=true"
    }

    data object SearchPage: Screen("/search/query"){
        fun searchByCategory(category: Category): String {
            return "/search/query?$CATEGORY_PARAM=${category.name}"
        }

        fun searchByTitle(query: String): String {
            return "/search/query?$QUERY_PARAM=$query"
        }
    }

    data object Home: Screen("/")
}
