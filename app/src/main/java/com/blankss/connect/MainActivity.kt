package com.blankss.connect

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blankss.connect.databinding.ActivityMainBinding
import com.blankss.connect.pkg.ApiClientBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val clientBuilder = ApiClientBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}