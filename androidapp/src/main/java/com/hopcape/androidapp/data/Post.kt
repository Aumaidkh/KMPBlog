package com.hopcape.androidapp.data

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

open class BlogPost: RealmObject {
    @PrimaryKey
    var _id: String = ""
    var author: String = ""
    var date: Long = 0L
    var title: String = ""
    var subtitle: String = ""
    var thumbnail: String = ""
    var content: String = ""
    var category: String = "Programming"
    var popular: Boolean = false
    var main: Boolean = false
    var sponsored: Boolean = false
}