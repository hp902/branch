package com.example.branch.ui.message

import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("id") var userName: Int,
    @SerializedName("thread_id") var threadId: Int,
    @SerializedName("user_id") var userId: String,
    @SerializedName("agent_id") var agentId: String? = null,
    @SerializedName("body") var bodyMessage: String,
    @SerializedName("timestamp") var timeStamp: String,
)

data class SendMessage(
    @SerializedName("thread_id") var threadId: Int,
    @SerializedName("body") var bodyMessage: String
)