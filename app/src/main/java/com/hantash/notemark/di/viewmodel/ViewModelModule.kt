package com.hantash.notemark.di.viewmodel

import com.hantash.notemark.data.api.NoteAPI
import com.hantash.notemark.data.repo.AuthRepository
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

}