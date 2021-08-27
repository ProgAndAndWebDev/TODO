package com.example.todo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todo.data.Repository

class TaskViewModelProvider(val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TaskViewModel(repository) as T
    }
}