package com.hantash.notemark.data.api

import com.hantash.notemark.model.AuthToken
import com.hantash.notemark.model.User
import com.hantash.notemark.utils.Constant
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface NoteAPI {
    @POST(Constant.BASE_URL + "/api/auth/register")
    suspend fun register(@Body payload: Map<String, String>): Response<User>

    @POST(Constant.BASE_URL + "/api/auth/login")
    suspend fun login(@Body payload: Map<String, String>): Response<AuthToken>
}