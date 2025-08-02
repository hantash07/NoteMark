package com.hantash.notemark.data.repo

import com.hantash.notemark.data.api.ExceptionAPI
import com.hantash.notemark.data.api.NoteAPI
import com.hantash.notemark.data.api.Resource
import com.hantash.notemark.data.db.NoteDao
import com.hantash.notemark.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NoteRepository(
    private val noteDao: NoteDao,
    private val noteAPI: NoteAPI,
) {
    val notesFlow: Flow<List<Note>> = noteDao.fetch()

    suspend fun addNoteLocal(note: Note) {
        noteDao.insert(note)
    }

    suspend fun updateNoteLocal(note: Note) {
        noteDao.update(note)
    }

    suspend fun deleteNoteLocal(note: Note) {
        noteDao.delete(note)
    }

    suspend fun clearNotes() {
        noteDao.clearNotes()
    }

    fun fetchById(id: String): Flow<Note?> {
        return noteDao.fetch(id)
    }

    fun getPendingNotes(): Flow<List<Note>> {
        return noteDao.getPendingNotes()
    }

    fun addNote(note: Note): Flow<Resource<Note?>> = flow {
        emit(Resource.Loading())
        try {
            val payload = mapOf("id" to note.id.toString(), "title" to note.title, "content" to note.content,
                "createdAt" to note.createdAt.toString(), "lastEditedAt" to note.lastEditedAt.toString())
            val response = noteAPI.addNote(payload)
            emit(Resource.Success(response.body()))
        } catch (exceptionAPI: ExceptionAPI) {
            emit(Resource.Error(exceptionAPI.message, exceptionAPI.code))
        } catch (exception: Exception) {
            emit(Resource.Error(exception.localizedMessage))
        }
    }

    fun updateNote(note: Note): Flow<Resource<Note?>> = flow {
        emit(Resource.Loading())
        try {
            val payload = mapOf("id" to note.id.toString(), "title" to note.title, "content" to note.content,
                "createdAt" to note.createdAt.toString(), "lastEditedAt" to note.lastEditedAt.toString())
            val response = noteAPI.updateNote(payload)
            emit(Resource.Success(response.body()))
        } catch (exceptionAPI: ExceptionAPI) {
            emit(Resource.Error(exceptionAPI.message, exceptionAPI.code))
        } catch (exception: Exception) {
            emit(Resource.Error(exception.localizedMessage))
        }
    }

    fun deleteNote(note: Note): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            val response = noteAPI.deleteNote(note.id.toString())
            emit(Resource.Success(response.body()))
        } catch (exceptionAPI: ExceptionAPI) {
            emit(Resource.Error(exceptionAPI.message, exceptionAPI.code))
        } catch (exception: Exception) {
            emit(Resource.Error(exception.localizedMessage))
        }
    }

    fun getNotes(): Flow<Resource<List<Note>?>> = flow {
        emit(Resource.Loading())
        try {
            val response = noteAPI.getNotes()
            emit(Resource.Success(response.body()))
        } catch (exceptionAPI: ExceptionAPI) {
            emit(Resource.Error(exceptionAPI.message, exceptionAPI.code))
        } catch (exception: Exception) {
            emit(Resource.Error(exception.localizedMessage))
        }
    }
}