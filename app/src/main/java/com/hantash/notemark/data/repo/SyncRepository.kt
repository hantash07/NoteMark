package com.hantash.notemark.data.repo

import com.hantash.notemark.data.api.NoteAPI
import com.hantash.notemark.data.db.SyncRecordDao
import com.hantash.notemark.model.SyncOperation
import com.hantash.notemark.model.SyncRecord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class SyncRepository(
    private val syncRecordDao: SyncRecordDao,
    val noteAPI: NoteAPI,
) {
//    val syncListFlow: Flow<List<SyncRecord>> = syncRecordDao.fetchByUserId()

    suspend fun addSync(record: SyncRecord) {
        syncRecordDao.insert(record)
    }

    suspend fun updateSync(record: SyncRecord) {
        syncRecordDao.update(record)
    }

    suspend fun deleteSync(record: SyncRecord) {
        syncRecordDao.delete(record)
    }

    suspend fun fetch(noteId: String): SyncRecord? {
        return syncRecordDao.fetch(noteId).firstOrNull()
    }

    suspend fun fetch(noteId: String, operation: SyncOperation): SyncRecord? {
        return syncRecordDao.fetch(noteId, operation).firstOrNull()
    }

    suspend fun clearRecords() {
        syncRecordDao.clearRecords()
    }

    fun fetchByUserId(id: String): Flow<List<SyncRecord>> {
        return syncRecordDao.fetchByUserId(id)
    }
}