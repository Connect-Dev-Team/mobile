package com.blankss.connect.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blankss.connect.R
import com.blankss.connect.data.User
import com.google.android.material.imageview.ShapeableImageView

class UserAdapter(private val dataUser: List<User>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    // ViewHolder class
    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userNameTextView: TextView = view.findViewById(R.id.nama)
        val emailTextView: TextView = view.findViewById(R.id.email)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_design, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = dataUser[position]
        holder.userNameTextView.text = user.username
        holder.emailTextView.text = user.email
    }

    override fun getItemCount(): Int = dataUser.size
}
