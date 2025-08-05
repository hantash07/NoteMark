package com.hantash.notemark.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hantash.notemark.model.SyncOperation
import com.hantash.notemark.model.SyncRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface SyncRecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: SyncRecord)

    @Update
    suspend fun update(record: SyncRecord)

    @Delete
    suspend fun delete(record: SyncRecord)

    @Query("DELETE FROM SyncRecord")
    suspend fun clearRecords()

    @Query("SELECT * FROM SyncRecord WHERE userId =:id")
    fun fetchByUserId(id: String): Flow<List<SyncRecord>>

    @Query("SELECT * FROM SyncRecord WHERE noteId =:noteId ")
    fun fetch(noteId: String): Flow<SyncRecord?>

    @Query("SELECT * FROM SyncRecord WHERE noteId =:noteId AND operation =:syncOperation")
    fun fetch(noteId: String, syncOperation: SyncOperation): Flow<SyncRecord?>
}