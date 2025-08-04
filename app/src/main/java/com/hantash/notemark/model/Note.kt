package com.hantash.notemark.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.UUID

@Entity
data class Note(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    var title: String = "Note Title",
    var content: String = "",
    val createdAt: String = Instant.now().toString(),
    var lastEditedAt: String = Instant.now().toString(),
)
