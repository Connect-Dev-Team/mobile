package com.blankss.connect.pkg

interface ApiInference {
    companion object {
        const val BASE_URL = "http://192.168.0.47:7002/api/"
        const val ENDPOINT_USERS = "v1/users"
        const val ENDPOINT_CHAT = "v1/messages"
        const val SOCKET_URL = "http://192.168.0.47:5001"
    }
}