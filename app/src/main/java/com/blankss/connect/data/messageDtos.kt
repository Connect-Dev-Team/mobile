package com.blankss.connect.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AuthorDto(
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("picture")
    val picture: String? = null // Picture bisa null
) {
    override fun toString(): String {
        return name
    }
}

data class MessageDto(
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("is_sent")
    val isSent: Boolean,

    @field:SerializedName("to")
    val to: AuthorDto,

    @field:SerializedName("from")
    val from: AuthorDto,

    @field:SerializedName("content")
    val content: String
) {
    override fun toString(): String {
        return "from: $from, to: $to, content: $content"
    }
}

data class CreateMessageDto(
    @field:SerializedName("to_id")
    val toId: String,

    @field:SerializedName("from_id")
    val fromId: String,

    @field:SerializedName("content")
    val content: String
)