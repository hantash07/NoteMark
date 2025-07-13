package com.hantash.notemark.data.api

import com.hantash.notemark.utils.debug
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ErrorInterceptor : Interceptor {
    private val jsonSerializable = Json {
        ignoreUnknownKeys = true
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .addHeader("X-User-Email", "hantashnadeem@gmail.com")
            .build()

        val response = chain.proceed(newRequest)
        if (!response.isSuccessful) {
            val errorBody = response.body?.string()
            debug("Response Error: $errorBody")
            val errorMessage = if (!errorBody.isNullOrBlank()) {
                try {
                    val badResponse = jsonSerializable.decodeFromString(BadResponse.serializer, errorBody)
                    when (badResponse) {
                        is BadResponse.Auth -> badResponse.error
                        is BadResponse.Conflict -> badResponse.reason
                    }
                } catch (e: Exception) {
                    debug("ErrorInterceptor: ${e.localizedMessage}")
                    "Error parsing response"
                }
            } else {
                debug("Response Code: ${response.code}")
                when (response.code) {
                    401 -> "Invalid Email id or password"
                    else -> "Unknown error occurred"
                }
            }
            throw ExceptionAPI(errorMessage, response.code)
        }
        return response
    }

}

class ExceptionAPI(message: String, val code: Int) : IOException(message)

@Serializable
sealed class BadResponse {
    @Serializable
    @SerialName("Conflict")
    data class Conflict(val status: String, val reason: String) : BadResponse()

    @Serializable
    @SerialName("Auth")
    data class Auth(val error: String, val status: Int) : BadResponse()

    companion object {
        val serializer = object : JsonContentPolymorphicSerializer<BadResponse>(BadResponse::class) {
            override fun selectDeserializer(element: JsonElement): KSerializer<out BadResponse> {
                val obj = element.jsonObject

                return when {
                    "reason" in obj -> Conflict.serializer()
                    "error" in obj -> Auth.serializer()
                    else -> throw SerializationException("Unknown error format: $element")
                }
            }
        }
    }
}
