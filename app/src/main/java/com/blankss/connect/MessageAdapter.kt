package com.blankss.connect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blankss.connect.data.MessageResponse

//ini adalah adapter tolong dipindah ^_^

class MessageAdapter(private val messageList: List<MessageResponse>) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messageList[position]

        if (message.isSent) {
            holder.sentMessageLayout.visibility = View.VISIBLE
            holder.receivedMessageLayout.visibility = View.GONE
            holder.sentMessageText.text = message.text
        } else {
            holder.sentMessageLayout.visibility = View.GONE
            holder.receivedMessageLayout.visibility = View.VISIBLE
            holder.receivedMessageText.text = message.text
        }
    }

    override fun getItemCount(): Int = messageList.size

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sentMessageLayout: View = itemView.findViewById(R.id.sentMessageLayout)
        val receivedMessageLayout: View = itemView.findViewById(R.id.receivedMessageLayout)
        val sentMessageText: TextView = itemView.findViewById(R.id.sentMessageText)
        val receivedMessageText: TextView = itemView.findViewById(R.id.receivedMessageText)
    }
}
