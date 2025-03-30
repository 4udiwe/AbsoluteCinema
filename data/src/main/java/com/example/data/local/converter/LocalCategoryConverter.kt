package com.example.data.local.converter

import androidx.room.TypeConverter
import com.example.domain.model.LocalCategory

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