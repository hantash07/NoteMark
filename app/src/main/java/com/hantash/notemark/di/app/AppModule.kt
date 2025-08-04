package com.hantash.notemark.di.app

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import com.hantash.notemark.data.api.ErrorInterceptor
import com.hantash.notemark.data.api.NoteAPI
import com.hantash.notemark.data.db.LocalDatabase
import com.hantash.notemark.data.db.NoteDao
import com.hantash.notemark.data.db.SyncRecordDao
import com.hantash.notemark.utils.Constant
import com.hantash.notemark.utils.dataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @AppScope
    fun retrofit(dataStore: DataStore<Preferences>): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(ErrorInterceptor(dataStore))
            .build()

        return Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @AppScope
    fun noteAPI(retrofit: Retrofit): NoteAPI {
        return retrofit.create(NoteAPI::class.java)
    }

    @Provides
    @AppScope
    fun dataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    @Provides
    @AppScope
    fun localDatabase(@ApplicationContext context: Context): LocalDatabase {
       return  Room.databaseBuilder(
            context,
            LocalDatabase::class.java,
            name = "local_db"
        ).fallbackToDestructiveMigrationFrom(true, 1).build()
    }

    @Provides
    @AppScope
    fun noteDao(localDatabase: LocalDatabase): NoteDao {
        return localDatabase.noteDao()
    }

    @Provides
    @AppScope
    fun syncRecordDao(localDatabase: LocalDatabase): SyncRecordDao {
        return localDatabase.syncRecordDao()
    }

}