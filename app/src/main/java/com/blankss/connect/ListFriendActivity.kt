package com.blankss.connect

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankss.connect.adapter.UserAdapter
import com.blankss.connect.data.StandardResponse
import com.blankss.connect.data.User
import com.blankss.connect.databinding.ActivityListfriendBinding
import com.blankss.connect.pkg.ApiClientBuilder
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListFriendActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListfriendBinding
    private val userAdapter = UserAdapter(arrayListOf(), object : UserAdapter.Events {
        override fun onClick(it: User) {
            val intent = Intent(this@ListFriendActivity, ChatroomActivity::class.java)
            intent.putExtra("id", it.id)
            intent.putExtra("name", it.username)
            intent.putExtra("image", it.profilePicture)

            startActivity(intent)
        }
    })
    private val apiClient = ApiClientBuilder().getUserServices()
    private val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListfriendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.datalist.apply {
            layoutManager = LinearLayoutManager(this@ListFriendActivity)
            adapter = userAdapter
        }

        fetchUser()
    }

    private fun fetchUser() {
        auth.currentUser?.getIdToken(true)?.addOnSuccessListener {
            apiClient.getUsers(it.token.toString()).enqueue(object : Callback<StandardResponse<ArrayList<User>>> {
                override fun onResponse(
                    p0: Call<StandardResponse<ArrayList<User>>>,
                    p1: Response<StandardResponse<ArrayList<User>>>
                ) {

                    if (p1.isSuccessful) {
                        userAdapter.dispatch(p1.body()!!.data)
                    }

                }

                override fun onFailure(p0: Call<StandardResponse<ArrayList<User>>>, p1: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }

    }
}
