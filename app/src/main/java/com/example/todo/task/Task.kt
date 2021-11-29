package com.example.todo.task

import java.io.Serializable

data class Task(val id: String, val title: String, val description: String = "Default") : Serializable
