package com.example.todo.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream
import java.util.*


class Converter {

    @TypeConverter
    fun fromTimeStamp(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun toTimeStamp(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun fromPrioraty(priority: Priority): String {
        return priority.name
    }

    @TypeConverter
    fun toPrioraty(priority: String): Priority {
        return Priority.valueOf(priority)
    }

    @TypeConverter
    fun fromBitMap(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 1000, outputStream)
        return outputStream.toByteArray()
    }

    @TypeConverter
    fun toBitMap(bytArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(bytArray, 0, bytArray.size)
    }
}