package com.task.branch.model

import com.google.gson.annotations.SerializedName

data class MessageRequest(

	@field:SerializedName("thread_id")
	val threadId: Int? = null,

	@field:SerializedName("body")
	val body: String? = null
)
