package com.hantash.notemark.data.repo

import com.hantash.notemark.data.api.ExceptionAPI
import com.hantash.notemark.data.api.NoteAPI
import com.hantash.notemark.data.api.Resource
import com.hantash.notemark.model.User
import com.hantash.notemark.utils.debug
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthRepository(private val noteAPI: NoteAPI) {
    /*
    * Suspended Functions can also be used. But I prefer flow as it can emit multiple values whereas suspend function returns only one.
    * Following flows used are Cold Flow.
    * Cold Flow means that the code inside the flow executed when there is a collector.
    * */

    fun register(username: String, email: String, password: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            val payload = mapOf("username" to username, "email" to email, "password" to password)
            val response = noteAPI.register(payload)
            emit(Resource.Success(response.body()))
        } catch (exceptionAPI: ExceptionAPI) {
            emit(Resource.Error(exceptionAPI.message))
        } catch (exception: Exception) {
            debug("register => ${exception.localizedMessage}")
            emit(Resource.Error(exception.localizedMessage))
        }
    }

    fun login(email: String, password: String): Flow<Resource<User?>> = flow {
        emit(Resource.Loading())
        try {
            val payload = mapOf("email" to email, "password" to password)
            val response = noteAPI.login(payload)
            val authToken = response.body()
            if (authToken != null) {
                emit(Resource.Success(authToken))
            } else {
                emit(Resource.Success(null))
            }
            emit(Resource.Success(response.body()))
        } catch (exceptionAPI: ExceptionAPI) {
            emit(Resource.Error(exceptionAPI.message))
        } catch (exception: Exception) {
            emit(Resource.Error(exception.localizedMessage))
        }
    }
}