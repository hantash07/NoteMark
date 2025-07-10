package com.hantash.notemark.data.api

import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ErrorInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .addHeader("X-User-Email", "hantashnadeem@gmail.com")
            .build()

        val response = chain.proceed(newRequest)
        if (!response.isSuccessful) {
            val errorBody = response.body?.string()
            val errorMessage = if (errorBody != null) {
                try {
                    val apiError = Gson().fromJson(errorBody, ErrorResponseAPI::class.java)
                    apiError.error
                } catch (e: Exception) {
                    "Error parsing response"
                }
            } else {
                when (response.code) {
                    401 -> "Invalid Email id or password"
                    else -> "Unknown error occurred"
                }
            }
            throw ExceptionErrorAPI(errorMessage, response.code)
        }
        return response
    }

}

class ExceptionErrorAPI(message: String, val code: Int) : IOException(message)

data class ErrorResponseAPI(val error: String, val status: Int)
