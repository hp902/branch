package com.example.branch.ui.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("auth_token") val token: String? = null
)

data class LoginError(
    @SerializedName("error") val error: String? = null
)

data class LoginModel(
    @SerializedName("username") var userName: String,
    @SerializedName("password") var password: String
)
