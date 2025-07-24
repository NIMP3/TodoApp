package dev.yovany.todoapp.data

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromCategoryList(categories: List<Int>): String = categories.joinToString(",")

    @TypeConverter
    fun toCategoryList(data: String): List<Int> = data.split(",").mapNotNull { it.toIntOrNull() }
}