package com.hantash.notemark.data.repo

import com.hantash.notemark.data.api.ExceptionErrorAPI
import com.hantash.notemark.data.api.NoteAPI
import com.hantash.notemark.data.api.Resource
import com.hantash.notemark.model.AuthToken
import com.hantash.notemark.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthRepository(private val noteAPI: NoteAPI) {
    /*
    * Suspended Functions can also be used. But I prefer flow as it can emit multiple values whereas suspend function returns only one.
    * Following flow used are Cold Flow.
    * */

    fun register(username: String, email: String, password: String): Flow<Resource<User>> = flow {
        emit(Resource.Loading())
        try {
            val payload = mapOf("username" to username, "email" to email, "password" to password)
            val response = noteAPI.register(payload)
            emit(Resource.Success(response.body()))
        } catch (exceptionAPI: ExceptionErrorAPI) {
            emit(Resource.Error(exceptionAPI.message))
        } catch (exception: Exception) {
            emit(Resource.Error(exception.localizedMessage))
        }
    }

    fun login(email: String, password: String): Flow<Resource<AuthToken>> = flow {
        emit(Resource.Loading())
        try {
            val payload = mapOf("email" to email, "password" to password)
            val response = noteAPI.login(payload)
            emit(Resource.Success(response.body()))
        } catch (exceptionAPI: ExceptionErrorAPI) {
            emit(Resource.Error(exceptionAPI.message))
        } catch (exception: Exception) {
            emit(Resource.Error(exception.localizedMessage))
        }
    }

}