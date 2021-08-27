package com.example.todo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.todo.model.Converter
import com.example.todo.model.Task


@Database(entities = [Task::class], version = 3, exportSchema = false)
@TypeConverters(Converter::class)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun getTask(): TaskDao

    companion object {

        private val MIGRATION_1_2: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE task_table ADD COLUMN address TEXT"
                )
            }
        }

        @Volatile
        private var instance: TaskDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                TaskDatabase::class.java,
                "task2_db.db"
            ).addMigrations(MIGRATION_1_2)
                .build()
    }

}