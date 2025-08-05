package com.hantash.notemark.di.viewmodel

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.hantash.notemark.data.api.NoteAPI
import com.hantash.notemark.data.db.NoteDao
import com.hantash.notemark.data.db.SyncRecordDao
import com.hantash.notemark.data.repo.AuthRepository
import com.hantash.notemark.data.repo.NoteRepository
import com.hantash.notemark.data.repo.PreferencesRepository
import com.hantash.notemark.data.repo.SyncRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class ViewModelModule {
    @Provides
    @ViewModelScoped
    fun authRepository(noteAPI: NoteAPI) = AuthRepository(noteAPI)

    @Provides
    @ViewModelScoped
    fun preferencesRepository(dataStore: DataStore<Preferences>) = PreferencesRepository(dataStore)

    @Provides
    @ViewModelScoped
    fun noteRepository(noteDao: NoteDao, noteAPI: NoteAPI) = NoteRepository(noteDao, noteAPI)

    @Provides
    @ViewModelScoped
    fun syncRepository(syncRecordDao: SyncRecordDao, noteAPI: NoteAPI) = SyncRepository(syncRecordDao, noteAPI)
}