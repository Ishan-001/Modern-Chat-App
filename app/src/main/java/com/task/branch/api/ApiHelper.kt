package com.task.branch.api

import com.task.branch.model.LoginRequest
import com.task.branch.model.LoginResponse
import com.task.branch.model.Message
import com.task.branch.model.MessageRequest
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiHelper {

    suspend fun login(loginRequest: LoginRequest) : Response<LoginResponse>

    suspend fun getMessages(authToken : String) : Response<List<Message>>

    suspend fun addMessage(authToken : String, messageRequest: MessageRequest) : Response<Message>

}