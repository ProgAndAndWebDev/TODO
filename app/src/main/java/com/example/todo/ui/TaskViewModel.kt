package com.example.todo.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.data.Repository
import com.example.todo.model.Priority
import com.example.todo.model.Task
import kotlinx.coroutines.launch
import java.util.*


class TaskViewModel(private val repository: Repository) : ViewModel() {

    private val _getTasks: MutableLiveData<List<Task>> = MutableLiveData()
    val getTasks: LiveData<List<Task>>
        get() = _getTasks

    private val _getItemTasks: MutableLiveData<List<Task>> = MutableLiveData()
    val getItemTasks: LiveData<List<Task>>
        get() = _getItemTasks

    init {
        viewModelScope.launch {
            _getTasks.value = (repository.getTask())
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            repository.insertTask(task)
            _getTasks.value = (repository.getTask())
        }
    }

    fun getItemTask(title: String) {
        viewModelScope.launch {
            _getItemTasks.value = repository.getQueryTask(title)
        }
    }


    fun deleteAllTasks() {
        viewModelScope.launch {
            repository.deleteAllTasks()
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
            _getTasks.value = (repository.getTask())
        }
    }

    fun updateTask(title: String, priority: Priority, dueDate: Date, createDate: Date, id: Long) {
        viewModelScope.launch {
            repository.editTask(title, priority, dueDate, createDate, id)
            _getTasks.value = (repository.getTask())
        }
    }

    fun updateOneTask(isDone: Boolean, id: Long) {
        viewModelScope.launch {
            repository.editOneTask(isDone, id)
            _getTasks.value = (repository.getTask())
        }
    }
}