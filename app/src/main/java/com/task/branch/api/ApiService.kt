package com.task.branch.api

import com.task.branch.model.LoginRequest
import com.task.branch.model.LoginResponse
import com.task.branch.model.Message
import com.task.branch.model.MessageRequest
import com.task.branch.util.Constants.AUTH_HEADER
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest) : Response<LoginResponse>

    @GET("messages")
    suspend fun getMessages(@Header(AUTH_HEADER) authHeader : String) : Response<List<Message>>

    @POST("messages")
    suspend fun addMessage(
        @Header(AUTH_HEADER) authToken : String,
        @Body messageRequest: MessageRequest
    ) : Response<Message>

}