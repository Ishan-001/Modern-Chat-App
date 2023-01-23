package com.task.branch.ui.adapter

import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.task.branch.R
import com.task.branch.databinding.ChatItemBinding
import com.task.branch.databinding.MessageItemBinding
import com.task.branch.model.Message
import com.task.branch.util.Utils
import java.time.ZonedDateTime

class MessageItemAdapter(private val chatList : ArrayList<Message>) : RecyclerView.Adapter<MessageItemAdapter.ChatViewHolder>() {

    inner class ChatViewHolder(
        private val itemBinding : MessageItemBinding
        ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(chatItem : Message) {
            itemBinding.apply {
                tvMessage.text = chatItem.body
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    tvDate.text = Utils.getParsedTimeStamp(chatItem.timestamp!!)
                else
                    tvDate.text = chatItem.timestamp

                if (chatItem.agentId != null) {
                    tvMessage.setTextColor(
                        ContextCompat.getColor(tvMessage.context, R.color.white)
                    )
                    root.setCardBackgroundColor(
                        ContextCompat.getColor(root.context, R.color.black)
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = MessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(chatList[position])
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    fun addMessage(item : Message) {
        chatList.add(item)
        notifyItemInserted(chatList.size - 1)
    }
}