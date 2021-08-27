package com.example.todo.util

import java.text.SimpleDateFormat
import java.util.*

class Utils {
    companion object{
        fun formatDay(date: Date):String{
            val simpleDateFormat:SimpleDateFormat = SimpleDateFormat.getDateInstance() as SimpleDateFormat
            simpleDateFormat.applyPattern("EEE")
            return simpleDateFormat.format(date)
        }

        fun formatDayNum(date: Date):String{
            val simpleDateFormat:SimpleDateFormat = SimpleDateFormat.getDateInstance() as SimpleDateFormat
            simpleDateFormat.applyPattern("MMM")
            return simpleDateFormat.format(date)
        }

        fun formatDayMonth(date: Date):String{
            val simpleDateFormat:SimpleDateFormat = SimpleDateFormat.getDateInstance() as SimpleDateFormat
            simpleDateFormat.applyPattern("dd")
            return simpleDateFormat.format(date)
        }

        fun formatTime(date: Date):String{
            val simpleDateFormat:SimpleDateFormat = SimpleDateFormat.getDateInstance() as SimpleDateFormat
            simpleDateFormat.applyPattern("h:mm a")
            return simpleDateFormat.format(date)
        }

        suspend fun formatTimeCurent(date: Date):String{
            val simpleDateFormat:SimpleDateFormat = SimpleDateFormat.getDateInstance() as SimpleDateFormat
            simpleDateFormat.applyPattern("h:mm:ss a")
            return simpleDateFormat.format(date)
        }

    }
}