package com.simplenotes.notes.data.local

import androidx.room.TypeConverter
import java.time.LocalDateTime

/**
 * Type converter for storage of timestamps in room as strings
 */
object LocalDateTimeTypeConverter {

    @TypeConverter
    @JvmStatic
    fun toLocalDateTime(value: String?): LocalDateTime? {
        return LocalDateTime.parse(value)
    }

    @TypeConverter
    @JvmStatic
    fun fromLocalDateTime(date: LocalDateTime?): String? {
        return date.toString()
    }
}