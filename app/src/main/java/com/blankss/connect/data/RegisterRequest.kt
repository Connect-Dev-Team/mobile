package com.blankss.connect.data

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @field:SerializedName("unique_id")
    val uniqueID: String,
    val name: String,
    val email: String,
    val picture: String,
)
