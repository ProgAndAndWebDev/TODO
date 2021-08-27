package com.example.todo.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*


@Entity(tableName = "task_table")
data class Task(
    @ColumnInfo(name = "task_id")
    @PrimaryKey(autoGenerate = true)
    val taskId: Long = 0,
    @ColumnInfo(name = "username")
    val username: String = "",
    @ColumnInfo(name = "task_title")
    val title: String,
    @ColumnInfo(name = "task_priority")
    val priority: Priority = Priority.LOW,
    @ColumnInfo(name = "due_Date")
    val dueDate: Date,
    @ColumnInfo(name = "create_Date")
    val createDate: Date,
    @ColumnInfo(name = "is_done")
    val isDone: Boolean = false
):Serializable
