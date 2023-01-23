package com.task.branch.util

import com.task.branch.model.Message

sealed class Screen {
    object Login : Screen()
    object Home : Screen()
    class Chat(val chat : List<Message>) : Screen()
}
