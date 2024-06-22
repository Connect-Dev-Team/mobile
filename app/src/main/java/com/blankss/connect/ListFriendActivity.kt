package com.blankss.connect

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankss.connect.adapter.UserAdapter
import com.blankss.connect.data.User
import com.blankss.connect.databinding.ActivityListfriendBinding
import com.blankss.connect.pkg.ApiClientBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListFriendActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListfriendBinding
    private val dataUser = mutableListOf<User>()
    private val userAdapter = UserAdapter(dataUser)
    private val apiClient = ApiClientBuilder().getUserServices()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListfriendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.datalist.apply {
            layoutManager = LinearLayoutManager(this@ListFriendActivity)
            adapter = userAdapter
        }

        FetchUserApi()
    }

    private fun FetchUserApi() {
        apiClient.getUsers().enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    response.body()?.let { users ->
                        dataUser.clear()
                        dataUser.addAll(users)
                        userAdapter.notifyDataSetChanged()
                    }
                } else {
                    Log.e("ListFriendActivity", "Failed to get response: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.e("ListFriendActivity", "API call failed: ${t.message}")
            }
        })
    }
}
