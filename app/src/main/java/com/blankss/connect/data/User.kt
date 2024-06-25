package com.blankss.connect.data

import com.google.gson.annotations.SerializedName

data class User(
    val id: String,
    val email: String,
    val username: String,
    @field:SerializedName("profile_picture")
    val profilePicture: String,
) {
    override fun toString(): String {
        return "{id: $id, email: $email, username: $username}"
    }
}
