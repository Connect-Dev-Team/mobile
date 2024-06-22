package com.blankss.connect

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankss.connect.data.Message
import com.blankss.connect.data.MessageResponse
import com.blankss.connect.databinding.ActivityChatroomBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.blankss.connect.pkg.ApiClientBuilder
import com.blankss.connect.pkg.ChatApi


class ChatroomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatroomBinding
    private val messageList = mutableListOf<Message>()
    private val messageAdapter = MessageAdapter(messageList)
    private lateinit var chatApi: ChatApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatroomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ChatroomActivity)
            adapter = messageAdapter
        }

        binding.sendButton.setOnClickListener {
            val messageText = binding.messageEditText.text.toString()
            if (messageText.isNotEmpty()) {
                val message = Message(messageText, true)
                messageList.add(message)
                messageAdapter.notifyItemInserted(messageList.size - 1)
                binding.recyclerView.scrollToPosition(messageList.size - 1)
                binding.messageEditText.text.clear()
            }
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        chatApi = ApiClientBuilder().getChatServices()

        fetchMessagesFromApi()
    }

    private fun fetchMessagesFromApi() {
        chatApi.getMessages().enqueue(object : Callback<List<MessageResponse>> {
            override fun onResponse(
                call: Call<List<MessageResponse>>,
                response: Response<List<MessageResponse>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { messages ->
                        messageList.clear()
                        messages.forEach { messageResponse ->
                            val message = Message(
                                text = messageResponse.text,
                                isSent = messageResponse.isSent
                            )
                            messageList.add(message)
                        }
                        messageAdapter.notifyDataSetChanged()
                    }
                } else {
                    Log.e("ChatroomActivity", "Gagal memuat pesan: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<List<MessageResponse>>, t: Throwable) {
                Log.e("ChatroomActivity", "Error cuy: ${t.message}")
            }
        })
    }
}
