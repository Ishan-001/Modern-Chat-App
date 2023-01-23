package com.task.branch.api

import com.task.branch.model.LoginRequest
import com.task.branch.model.LoginResponse
import com.task.branch.model.Message
import com.task.branch.model.MessageRequest
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(
    private val apiService: ApiService
    ) : ApiHelper {

    override suspend fun login(loginRequest: LoginRequest): Response<LoginResponse> = apiService.login(loginRequest)

    override suspend fun getMessages(authToken : String): Response<List<Message>> = apiService.getMessages(authToken)

    override suspend fun addMessage(authToken : String, messageRequest: MessageRequest): Response<Message> =
        apiService.addMessage(authToken, messageRequest)
}