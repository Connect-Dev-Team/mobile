package com.blankss.connect.pkg

import com.blankss.connect.data.MessageResponse
import retrofit2.Call
import retrofit2.http.GET

interface ChatApi {
    @GET("messages")
    fun getMessages(): Call<List<MessageResponse>>
}
