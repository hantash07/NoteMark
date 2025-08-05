package com.hantash.notemark.data.api

import com.hantash.notemark.model.Note
import com.hantash.notemark.model.NotesResponse
import com.hantash.notemark.model.User
import com.hantash.notemark.utils.Constant
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface NoteAPI {
    @POST(Constant.BASE_URL + "/api/auth/register")
    suspend fun register(@Body payload: Map<String, String>): Response<Unit>

    @POST(Constant.BASE_URL + "/api/auth/login")
    suspend fun login(@Body payload: Map<String, String>): Response<User?>

    @POST(Constant.BASE_URL + "/api/auth/logout")
    suspend fun logout(@Body payload: Map<String, String>): Response<Unit>

    @POST(Constant.BASE_URL + "/api/notes")
    suspend fun addNote(@Body payload: Map<String, String>): Response<Note?>

    @PUT(Constant.BASE_URL + "/api/notes")
    suspend fun updateNote(@Body payload: Map<String, String>): Response<Note?>

    @DELETE(Constant.BASE_URL + "/api/notes/{id}")
    suspend fun deleteNote(@Path("id") noteId: String): Response<Unit>

    @GET(Constant.BASE_URL + "/api/notes")
    suspend fun getNotes(@Query("page") page: Int = -1, @Query("size") size: Int = 0): Response<NotesResponse?>
}