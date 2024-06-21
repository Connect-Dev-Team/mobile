package com.blankss.connect

import android.R
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.blankss.connect.databinding.ActivityMainBinding
import com.blankss.connect.databinding.ActivitySearchBinding
import com.blankss.connect.pkg.ApiClientBuilder

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding

    private val clientBuilder = ApiClientBuilder()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lateinit var binding: ActivitySearchBinding

        val clientBuilder = ApiClientBuilder()

        val user = arrayOf("wahyu","rendi","manan","serli","matilda","dito","alfi","hasbi","syaipul")

        val userAdapter : ArrayAdapter<String> = ArrayAdapter(
            this, R.layout.simple_list_item_1,
            user
        )
        binding.userList.adapter = userAdapter

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()
                if (user.contains(query)){
                    userAdapter.filter.filter(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                userAdapter.filter.filter(newText)
                return false
            }

        })
    }
}