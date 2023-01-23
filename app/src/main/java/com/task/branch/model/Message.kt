package com.task.branch.model

import com.google.gson.annotations.SerializedName

data class Message(

	@field:SerializedName("thread_id")
	val threadId: Int? = null,

	@field:SerializedName("agent_id")
	val agentId: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("body")
	val body: String? = null,

	@field:SerializedName("timestamp")
	var timestamp: String? = null
)