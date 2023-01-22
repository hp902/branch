package com.example.branch.network

import com.example.branch.network.APIEndPoints.Companion.LOGIN
import com.example.branch.network.APIEndPoints.Companion.MESSAGE
import com.example.branch.ui.login.LoginModel
import com.example.branch.ui.login.LoginResponse
import com.example.branch.ui.message.Message
import com.example.branch.ui.message.SendMessage
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiMethods {

    @POST(LOGIN)
    suspend fun login(@Body loginModel: LoginModel): Response<LoginResponse>

    @GET(MESSAGE)
    suspend fun getMessages(@Header("X-Branch-Auth-Token") header: String): Response<List<Message>>

    @POST(MESSAGE)
    suspend fun sendMessage(
        @Body sendMessage: SendMessage,
        @Header("X-Branch-Auth-Token") header: String
    ): Response<Message>

}