package com.example.warre_welvaert_project_android.data

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class DbTypeConverter {
    companion object {
        private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    }
    @TypeConverter
    fun localTimeListFromString(value: String?): List<LocalTime> {
        if (value.isNullOrEmpty()) return emptyList()

        return value.split(",").mapNotNull {
            try {
                LocalTime.parse(it.trim(), timeFormatter)
            } catch (e: Exception) {
                null
            }
        }
    }

    @TypeConverter
    fun localTimeListToString(list: List<LocalTime>): String {
        return list.joinToString(",") { it.format(timeFormatter) }
    }

    @TypeConverter
    fun dateFromString(value: String?): LocalDateTime? {
        return value?.let {
            return LocalDateTime.parse(it, dateFormatter)
        }
    }

    @TypeConverter
    fun dateToString(date: LocalDateTime?): String? {
        return date?.format(dateFormatter)
    }
}