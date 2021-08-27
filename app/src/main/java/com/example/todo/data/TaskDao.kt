package com.example.todo.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.todo.model.Priority
import com.example.todo.model.Task
import java.util.*

@Dao
interface TaskDao {

    @Insert
    suspend fun addTask(task: Task)

    @Query("DELETE FROM task_table")
    suspend fun deleteAllTask()

    @Query("SELECT * FROM task_table")
    suspend fun getAllTask(): List<Task>

    @Query("SELECT COUNT(task_id) FROM task_table")
    suspend fun getCount(): Int

    @Query("SELECT * FROM task_table WHERE task_title LIKE '%' || :title || '%' ")
    suspend fun getOneTask(title:String): List<Task>

    @Query("UPDATE task_table SET is_done =:isDone WHERE task_id=:id")
    suspend fun updateOneTask(isDone: Boolean, id: Long)

    @Query("UPDATE task_table SET task_title = :title ,task_priority= :priority,due_Date= :dueDate,create_Date= :createDate WHERE task_id LIKE :id")
    suspend fun updateTask(title:String, priority: Priority, dueDate: Date,createDate: Date,id: Long)

    @Delete
    suspend fun deleteTask(task: Task)
}