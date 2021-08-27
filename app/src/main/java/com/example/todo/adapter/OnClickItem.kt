package com.example.todo.adapter

import com.example.todo.model.Task


interface OnClickItem {
    fun longClickItem(task: Task)
    fun clickCheckTask(id:Long)
    fun clickBackTask(id:Long)
}