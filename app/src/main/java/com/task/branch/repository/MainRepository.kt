package com.task.branch.repository

import com.task.branch.api.ApiHelper
import com.task.branch.model.LoginRequest
import com.task.branch.model.MessageRequest
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiHelper: ApiHelper
) {
    suspend fun login(loginRequest: LoginRequest) = apiHelper.login(loginRequest)
    suspend fun getMessages(authToken : String) = apiHelper.getMessages(authToken)
    suspend fun addMessage(authToken : String, messageRequest: MessageRequest) = apiHelper.addMessage(authToken, messageRequest)
}