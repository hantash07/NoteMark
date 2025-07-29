package com.hantash.notemark.data.repo

import com.hantash.notemark.data.db.NoteDao
import com.hantash.notemark.model.Note
import kotlinx.coroutines.flow.Flow

class NoteRepository(private val noteDao: NoteDao) {

    val notesFlow: Flow<List<Note>> = noteDao.fetch()

    suspend fun addNote(note: Note) {
        noteDao.insert(note)
    }

    suspend fun updateNote(note: Note) {
        noteDao.update(note)
    }

    suspend fun deleteNote(note: Note) {
        noteDao.delete(note)
    }

    suspend fun clearNotes() {
        noteDao.clearNotes()
    }

    fun fetchById(id: String): Flow<Note?> {
        return noteDao.fetch(id)
    }

}