package com.task.branch.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("auth_token")
	val authToken: String? = null
)
