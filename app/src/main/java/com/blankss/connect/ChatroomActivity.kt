package com.blankss.connect

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankss.connect.databinding.ActivityChatroomBinding


class  ChatroomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatroomBinding
    private val messageList = mutableListOf<Message>()
    private val messageAdapter = MessageAdapter(messageList)

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
    }
}
