package com.hantash.notemark.model

data class NotesResponse(
    val notes: List<Note>,
    val total: Int
)