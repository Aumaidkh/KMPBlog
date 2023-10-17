package com.hopcape.blog.models

import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.graphics.Colors

sealed class Message(val data: String){
    data class Info(val message: String): Message(message)
    data class Error(val message: String): Message(message)
    data class Success(val message: String): Message(message)

    fun getProgressBarTypeClass(): String {
        return when(this){
            is Error -> "bg-danger"
            is Info -> "bg-info"
            is Success -> "bg-success"
        }
    }

    fun getMessageTitle(): String {
        return Message::class.simpleName.toString()
    }

    fun getColor(): Color{
        return when(this){
            is Message.Error -> Colors.Red
            is Message.Info -> Colors.Yellow
            is Message.Success -> Colors.Green
        }
    }
}