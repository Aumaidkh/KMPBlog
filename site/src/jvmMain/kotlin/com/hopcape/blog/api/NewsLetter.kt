package com.hopcape.blog.api

import com.hopcape.blog.data.MongoDB
import com.hopcape.blog.models.NewsLetter
import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import com.varabyte.kobweb.api.data.getValue

@Api("subscribe")
suspend fun subscribeNewsLetter(context: ApiContext){
    try {
        val newsLetter = context.req.getBody<NewsLetter>()
        context.res.setBody(
            newsLetter?.let { context.data.getValue<MongoDB>().subscribe(it) }
        )
    } catch (e: Exception){
        context.res.setException(e)
    }
}