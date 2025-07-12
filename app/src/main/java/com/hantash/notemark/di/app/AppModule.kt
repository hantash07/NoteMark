package com.hantash.notemark.di.app

import com.hantash.notemark.data.api.ErrorInterceptor
import com.hantash.notemark.data.api.NoteAPI
import com.hantash.notemark.utils.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @AppScope
    fun retrofit(): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(ErrorInterceptor())
            .build()

        return Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @AppScope
    fun noteAPI(retrofit: Retrofit) : NoteAPI {
        return retrofit.create(NoteAPI::class.java)
    }

}