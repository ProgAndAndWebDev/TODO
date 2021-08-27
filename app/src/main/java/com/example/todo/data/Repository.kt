package com.example.todo.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.todo.model.Priority
import com.example.todo.model.Task
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class Repository(val taskDatabase: TaskDatabase) {

    suspend fun getTask() = taskDatabase.getTask().getAllTask()

    suspend fun insertTask(task: Task) {
        taskDatabase.getTask().addTask(task)
    }

    suspend fun getQueryTask(title: String) = taskDatabase.getTask().getOneTask(title)

    suspend fun getCountTask():Int = taskDatabase.getTask().getCount()

    suspend fun deleteAllTasks() {
        taskDatabase.getTask().deleteAllTask()
    }

    suspend fun deleteTask(task: Task) {
        taskDatabase.getTask().deleteTask(task)
    }

    suspend fun editTask(
        title: String,
        priority: Priority,
        dueDate: Date,
        createDate: Date,
        id: Long
    ) {
        taskDatabase.getTask().updateTask(title, priority, dueDate, createDate, id)
    }

    suspend fun editOneTask(isDone: Boolean, id: Long) {
        taskDatabase.getTask().updateOneTask(isDone, id)
    }
}