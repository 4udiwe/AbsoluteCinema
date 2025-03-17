package com.example.data.local.category

import androidx.room.TypeConverter

class LocalCategoryConverter {

    @TypeConverter
    fun fromLocalCategory(category: LocalCategory): String {
        return category.name
    }

    @TypeConverter
    fun toLocalCategory(value: String): LocalCategory {
        return LocalCategory.valueOf(value)
    }
}