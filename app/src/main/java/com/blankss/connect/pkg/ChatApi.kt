package com.blankss.connect.pkg

import com.blankss.connect.data.MessageDto
import com.blankss.connect.data.MessageResponse
import com.blankss.connect.data.StandardResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ChatApi {
    @GET(ApiInference.ENDPOINT_CHAT)
    fun getMessages(
        @Query("sender") sender: String,
        @Query("receiver") receiver: String,
        @Header("Authorization") token: String
    ): Call<StandardResponse<ArrayList<MessageDto>>>
}
