package com.hantash.notemark.data.db

import androidx.room.TypeConverter
import com.hantash.notemark.model.SyncOperation
import java.time.Instant

class DataConverter {
    @TypeConverter
    fun fromSyncOperation(operation: SyncOperation): String = operation.name

    @TypeConverter
    fun toSyncOperation(value: String): SyncOperation = SyncOperation.valueOf(value)

    @TypeConverter
    fun fromInstant(instant: Instant?): String? = instant?.toString() // ISO 8601

    @TypeConverter
    fun toInstant(value: String?): Instant? = value?.let { Instant.parse(it) }
}