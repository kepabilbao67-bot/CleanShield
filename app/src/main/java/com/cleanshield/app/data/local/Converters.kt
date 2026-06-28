package com.cleanshield.app.data.local

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalDateTime

class Converters {
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? = date?.toString()

    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? = dateString?.let { LocalDate.parse(it) }

    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime?): String? = dateTime?.toString()

    @TypeConverter
    fun toLocalDateTime(dateTimeString: String?): LocalDateTime? =
        dateTimeString?.let { LocalDateTime.parse(it) }
}
