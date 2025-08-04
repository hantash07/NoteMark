package com.hantash.notemark.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class SyncRecord(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val noteId: String,
    val operation: SyncOperation,
    val payload: String,
    val timestamp: Long,
)

enum class SyncOperation {
    CREATE,
    UPDATE,
    DELETE,
}