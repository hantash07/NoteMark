package com.hantash.notemark.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.time.Instant
import java.time.ZoneId
import java.util.UUID

@Entity
data class Note(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    var title: String = "Note Title",
    var content: String = "",
    val createdAt: Instant = Instant.now(),
    var lastEditedAt: Instant = Instant.now()
)

class Converters {
    @TypeConverter
    fun fromUUID(uuid: UUID?): String? = uuid?.toString()

    @TypeConverter
    fun toUUID(uuid: String?): UUID? = uuid?.let { UUID.fromString(it) }

    @TypeConverter
    fun fromInstant(instant: Instant?): String? = instant?.toString() // ISO 8601

    @TypeConverter
    fun toInstant(value: String?): Instant? = value?.let { Instant.parse(it) }
}