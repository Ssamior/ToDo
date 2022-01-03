package com.example.todo.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.todo.task.TasksRepository

class TaskListViewModel : ViewModel() {

    private val repository = TasksRepository();

    private val _taskList = MutableStateFlow<List<Task>>(emptyList());
    public val taskList: StateFlow<List<Task>> = _taskList;

    fun refresh() {
        viewModelScope.launch {
            val taskList = repository.refresh();
            if (taskList != null) {
                _taskList.value = taskList
            }
        }
    }

    fun delete(task: Task) {
        viewModelScope.launch {
            if (repository.deleteTask(task.id)) {
                _taskList.value = taskList.value - task;
            }
        }
    }

    fun addOrEdit(task: Task) {
        viewModelScope.launch {
            val oldTask = taskList.value.firstOrNull { it.id == task.id }
            val newTask = when {
                oldTask != null -> repository.updateTask(task);
                else -> repository.createTask(task);
            }
            //val newTask = if (oldTask != null) repository.updateTask(task) else repository.createTask(task)
            if (newTask != null) {
                if (oldTask != null) {
                    _taskList.value = taskList.value - oldTask;
                }
                _taskList.value = taskList.value + task;
            }
        }
    }
}
