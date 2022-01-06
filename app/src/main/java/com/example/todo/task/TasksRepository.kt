package com.example.todo.task

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.http.Body
import retrofit2.http.Path

class TasksRepository {
    private val tasksWebService = Api.tasksWebService

    suspend fun refresh() : List<Task>? {
        // Call HTTP (opération longue):
        val tasksResponse = tasksWebService.getTasks()
        // À la ligne suivante, on a reçu la réponse de l'API:
        if (tasksResponse.isSuccessful) {
            return tasksResponse.body()
        }
        return null
    }

    suspend fun updateTask(task: Task): Task? {
        val response = tasksWebService.update(task, task.id);
        if(response.isSuccessful) {
            return response.body();
        }
        return null
    }

    suspend fun createTask(task: Task): Task? {
        val response = tasksWebService.create(task)
        if(response.isSuccessful) {
            return response.body();
        }
        return null;
    }

    suspend fun deleteTask(id: String): Boolean {
        val response = tasksWebService.delete(id)
        if(response.isSuccessful) {
            return true
        }
        return false
    }
}
