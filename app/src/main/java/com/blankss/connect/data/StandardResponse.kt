package com.blankss.connect.data

data class StandardResponse<T>(
    val status: String,
    val message: String,
    val data: T
)
