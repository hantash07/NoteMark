package com.hantash.notemark.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hantash.notemark.model.Note
import com.hantash.notemark.model.SyncRecord

@Database(entities = [Note::class, SyncRecord::class], version = 4, exportSchema = false)
@TypeConverters(DataConverter::class)
abstract class LocalDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun syncRecordDao(): SyncRecordDao
}