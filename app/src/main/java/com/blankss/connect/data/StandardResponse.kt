package com.blankss.connect.data

data class StandardResponse<T>(
    val test: String,
    val message: String,
    val data: T
)
