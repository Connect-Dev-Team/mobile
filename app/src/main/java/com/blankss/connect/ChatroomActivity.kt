package com.blankss.connect

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankss.connect.adapter.MessageAdapter
import com.blankss.connect.data.Message
import com.blankss.connect.data.MessageDto
import com.blankss.connect.data.StandardResponse
import com.blankss.connect.databinding.ActivityChatroomBinding
import com.blankss.connect.pkg.ApiClientBuilder
import com.blankss.connect.pkg.ApiInference
import com.blankss.connect.pkg.ChatApi
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import io.socket.client.IO
import io.socket.client.Socket
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChatroomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatroomBinding
    private val messageAdapter = MessageAdapter(arrayListOf())
    private lateinit var chatApi: ChatApi
    private lateinit var mSocket: Socket

    init {
        try {
            chatApi = ApiClientBuilder().getChatServices()
            mSocket = IO.socket(ApiInference.SOCKET_URL)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatroomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mSocket.connect();

        mSocket.emit("regist_user", Firebase.auth.currentUser?.uid)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ChatroomActivity)
            adapter = messageAdapter
        }

        binding.sendButton.setOnClickListener {
            val messageText = binding.messageEditText.text.toString()
            if (messageText.isNotEmpty()) {
                val message = Message(messageText, true)
//                messageList.add(message)
//                messageAdapter.notifyItemInserted(messageList.size - 1)
//                binding.recyclerView.scrollToPosition(messageList.size - 1)
                binding.messageEditText.text.clear()
            }
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        fetchMessagesFromApi()
    }

    private fun fetchMessagesFromApi() {
        val sender = Firebase.auth.currentUser?.uid
        val receiver = intent.getStringExtra("id")

        Firebase.auth.currentUser?.getIdToken(true)?.addOnSuccessListener {
            Log.i("TOKEN GENERATED : ", it.token.toString())
            chatApi.getMessages(
                sender!!,
                receiver!!,
                it.token.toString()
                ).enqueue(object : Callback<StandardResponse<ArrayList<MessageDto>>> {
                override fun onResponse(
                    p0: Call<StandardResponse<ArrayList<MessageDto>>>,
                    p1: Response<StandardResponse<ArrayList<MessageDto>>>
                ) {
                    if (p1.isSuccessful) {
                        Log.i("SUKSES : ", "TRUE")
                        messageAdapter.dipatch(p1.body()!!.data)
                    } else {
                        Log.i("GAGAL:", "TRUE")
                    }
                }

                override fun onFailure(
                    p0: Call<StandardResponse<ArrayList<MessageDto>>>,
                    p1: Throwable
                ) {

                }
            })
        }

    }

    override fun onDestroy() {
        super.onDestroy()

        mSocket.emit("delete_user", intent.getStringExtra("id"))

        mSocket.disconnect()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        finish()
    }
}
