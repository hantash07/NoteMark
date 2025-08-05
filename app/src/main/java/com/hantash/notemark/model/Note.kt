package com.hantash.notemark.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.Instant
import java.util.UUID

@Serializable
@Entity
data class Note(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    var title: String = "Note Title",
    var content: String = "",
    val createdAt: String = Instant.now().toString(),
    var lastEditedAt: String = Instant.now().toString(),
) {
    fun payload() = Json.encodeToString(this)
}
