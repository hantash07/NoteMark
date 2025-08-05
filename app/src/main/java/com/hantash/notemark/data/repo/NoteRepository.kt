package com.hantash.notemark.data.repo

import com.hantash.notemark.data.api.ExceptionAPI
import com.hantash.notemark.data.api.NoteAPI
import com.hantash.notemark.data.api.Resource
import com.hantash.notemark.data.db.NoteDao
import com.hantash.notemark.model.Note
import com.hantash.notemark.model.NotesResponse
import com.hantash.notemark.utils.debug
import com.hantash.notemark.utils.toMap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NoteRepository(
    private val noteDao: NoteDao,
    private val noteAPI: NoteAPI,
) {
    val notesFlow: Flow<List<Note>> = noteDao.fetch()

    suspend fun addNote(note: Note) {
        noteDao.insert(note)
    }

    suspend fun addNote(notes: List<Note>) {
        noteDao.insert(notes)
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

    fun requestAddNote(payload: String): Flow<Resource<Note?>> = flow {
        emit(Resource.Loading())
        try {
//            val payload = mapOf("id" to note.id, "title" to note.title, "content" to note.content,
//                "createdAt" to note.createdAt, "lastEditedAt" to note.lastEditedAt)
            debug("Payload: $payload")
            val response = noteAPI.addNote(payload.toMap())
            emit(Resource.Success(response.body()))
        } catch (exceptionAPI: ExceptionAPI) {
            emit(Resource.Error(exceptionAPI.message, exceptionAPI.code))
        } catch (exception: Exception) {
            emit(Resource.Error(exception.localizedMessage))
        }
    }

    fun requestUpdateNote(payload: String): Flow<Resource<Note?>> = flow {
        emit(Resource.Loading())
        try {
//            val payload = mapOf("id" to note.id.toString(), "title" to note.title, "content" to note.content,
//                "createdAt" to note.createdAt.toString(), "lastEditedAt" to note.lastEditedAt.toString())
            val response = noteAPI.updateNote(payload.toMap())
            emit(Resource.Success(response.body()))
        } catch (exceptionAPI: ExceptionAPI) {
            emit(Resource.Error(exceptionAPI.message, exceptionAPI.code))
        } catch (exception: Exception) {
            emit(Resource.Error(exception.localizedMessage))
        }
    }

    fun requestDeleteNote(noteId: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            val response = noteAPI.deleteNote(noteId)
            emit(Resource.Success(response.body()))
        } catch (exceptionAPI: ExceptionAPI) {
            emit(Resource.Error(exceptionAPI.message, exceptionAPI.code))
        } catch (exception: Exception) {
            emit(Resource.Error(exception.localizedMessage))
        }
    }

    fun requestGetNotes(): Flow<Resource<NotesResponse?>> = flow {
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