package com.hantash.notemark.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.UUID

@Entity
data class SyncRecord(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val noteId: String,
    val operation: SyncOperation,
    var payload: String,
    var timestamp: Long = Instant.now().toEpochMilli(),
)

enum class SyncOperation {
    CREATE,
    UPDATE,
    DELETE,
}