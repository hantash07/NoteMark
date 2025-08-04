package com.hantash.notemark.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.hantash.notemark.model.SyncRecord

@Dao
interface SyncRecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(syncRecord: SyncRecord)

    @Update
    suspend fun update(syncRecord: SyncRecord)

    @Delete
    suspend fun delete(syncRecord: SyncRecord)
}