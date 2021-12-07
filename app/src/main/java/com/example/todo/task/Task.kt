package com.example.todo.task

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class Task(
    @SerialName("id")
    val id: String,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String = "Default"
) : java.io.Serializable
