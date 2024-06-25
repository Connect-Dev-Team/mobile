package com.blankss.connect.adapter

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.blankss.connect.R
import com.blankss.connect.data.User
import com.google.android.material.imageview.ShapeableImageView

class UserAdapter(private val dataUser: ArrayList<User>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    // ViewHolder class
    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userNameTextView: TextView = view.findViewById(R.id.nama)
        val imageView: ShapeableImageView = view.findViewById(R.id.image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_design, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = dataUser[position]
        holder.userNameTextView.text = user.username
        val imageUri = Uri.parse(user.profilePicture)
        holder.imageView.load(imageUri)
    }

    override fun getItemCount(): Int = dataUser.size

    @SuppressLint("NotifyDataSetChanged")
    fun dispatch(data: ArrayList<User>) {
        dataUser.clear()
        dataUser.addAll(data)
        notifyDataSetChanged()
    }
}
