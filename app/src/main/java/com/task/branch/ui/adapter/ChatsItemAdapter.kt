package com.task.branch.ui.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.task.branch.databinding.ChatItemBinding
import com.task.branch.model.Message
import com.task.branch.util.Utils
import java.time.ZonedDateTime

class ChatsItemAdapter(
    private val chatsList : List<ArrayList<Message>>,
    private val listener : ChatClickListener
) : RecyclerView.Adapter<ChatsItemAdapter.ChatViewHolder>() {

    inner class ChatViewHolder(
        private val itemBinding : ChatItemBinding
        ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(chat: ArrayList<Message>) {
            val chatItem = chat.last()
            itemBinding.apply {
                tvName.text = chatItem.userId.toString()
                tvBody.text = chatItem.body
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    tvDate.text = Utils.getParsedTimeStamp(chatItem.timestamp!!)
                else
                    tvDate.text = chatItem.timestamp

                root.setOnClickListener { listener.onChatClick(chat) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = ChatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(chatsList[position])
    }

    override fun getItemCount(): Int {
        return chatsList.size
    }

    interface ChatClickListener {
        fun onChatClick(chat : List<Message>)
    }
}