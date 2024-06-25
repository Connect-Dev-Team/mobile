package com.blankss.connect

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankss.connect.adapter.MessageAdapter
import com.blankss.connect.data.CreateMessageDto
import com.blankss.connect.data.Message
import com.blankss.connect.data.MessageDto
import com.blankss.connect.data.StandardResponse
import com.blankss.connect.databinding.ActivityChatroomBinding
import com.blankss.connect.pkg.ApiClientBuilder
import com.blankss.connect.pkg.ApiInference
import com.blankss.connect.pkg.ChatApi
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChatroomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatroomBinding
    private lateinit var messageAdapter: MessageAdapter
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

        mSocket.on("new_message"
        ) { args ->
            val gson = Gson()
            val concrete = gson.fromJson<MessageDto>(args[0].toString(), MessageDto::class.java)
            Log.i("DATA SOCKET : ", concrete.content)
            CoroutineScope(Dispatchers.Main).launch {
                messageAdapter.updatePartial(concrete)
            }

        }

        binding.username.text = intent.getStringExtra("name")

        messageAdapter = MessageAdapter(arrayListOf())

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ChatroomActivity)
            adapter = messageAdapter
        }

        binding.sendButton.setOnClickListener {
            val messageText = binding.messageEditText.text.toString()
            if (messageText.isNotEmpty()) {
                createMessage()
                binding.recyclerView.scrollToPosition(messageAdapter.messageList.size - 1)
                binding.messageEditText.text.clear()
            }
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        fetchMessagesFromApi()
    }

    fun createMessage() {
        val req = CreateMessageDto(
            toId = intent.getStringExtra("id")!!,
            fromId = Firebase.auth.currentUser!!.uid,
            content = binding.messageEditText.text.toString()
        )

        Firebase.auth.currentUser?.getIdToken(true)?.addOnSuccessListener {
            chatApi.createMessage(it.token!!, req).enqueue(object : Callback<StandardResponse<MessageDto>> {
                override fun onResponse(p0: Call<StandardResponse<MessageDto>>, p1: Response<StandardResponse<MessageDto>>) {
                    Toast.makeText(this@ChatroomActivity, "Berhasil menambah pesan", Toast.LENGTH_SHORT).show()
                    messageAdapter.updatePartial(p1.body()!!.data)
                }

                override fun onFailure(p0: Call<StandardResponse<MessageDto>>, p1: Throwable) {

                }
            })
        }
    }

    private fun fetchMessagesFromApi() {
        val sender = Firebase.auth.currentUser?.uid
        val receiver = intent.getStringExtra("id")

        Firebase.auth.currentUser?.getIdToken(true)?.addOnSuccessListener {
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
                        messageAdapter.dispatch(p1.body()!!.data)
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
