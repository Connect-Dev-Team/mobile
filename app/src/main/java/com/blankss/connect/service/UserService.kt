package com.blankss.connect.service

import com.blankss.connect.data.EmptyResponse
import com.blankss.connect.data.RegisterRequest
import com.blankss.connect.data.StandardResponse
import com.blankss.connect.data.User
import com.blankss.connect.pkg.ApiInference
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST

interface UserService {
    @GET(ApiInference.ENDPOINT_USERS)
    fun getUsers(@Header("Authorization") token: String): Call<StandardResponse<ArrayList<User>>>

    @POST(ApiInference.ENDPOINT_USERS)
    fun createUser(@Header("Authorization") token: String, @Body body: RegisterRequest): Call<Void>

    @PATCH(ApiInference.ENDPOINT_USERS)
    fun updateUser(): Call<EmptyResponse>
}