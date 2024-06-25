package com.blankss.connect.adapter

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.blankss.connect.R
import com.blankss.connect.data.User
import com.google.android.material.imageview.ShapeableImageView

class UserAdapter(private val dataUser: ArrayList<User>, var events: UserAdapter.Events) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

        interface Events {
            fun onClick(it: User)
        }

    // ViewHolder class
    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userNameTextView: TextView = view.findViewById(R.id.nama)
        val imageView: ShapeableImageView = view.findViewById(R.id.image)
        val card: CardView = view.findViewById(R.id.list_item)
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

        holder.card.setOnClickListener {
            events.onClick(user)
        }
    }

    override fun getItemCount(): Int = dataUser.size

    @SuppressLint("NotifyDataSetChanged")
    fun dispatch(data: ArrayList<User>) {
        dataUser.clear()
        dataUser.addAll(data)
        notifyDataSetChanged()
    }
}
