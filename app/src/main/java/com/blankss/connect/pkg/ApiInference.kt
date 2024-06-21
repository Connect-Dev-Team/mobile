package com.blankss.connect.pkg

interface ApiInference {
    companion object {
        const val BASE_URL = "https://localhost:7000/"
        const val ENDPOINT_USERS = "v1/users"
        const val ENDPOINT_CHAT = "v1/message"
    }
}