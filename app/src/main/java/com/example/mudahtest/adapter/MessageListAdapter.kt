package com.example.mudahtest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mudahtest.databinding.ViewChatMessageInboundBinding
import com.example.mudahtest.databinding.ViewChatMessageOutboundBinding
import com.example.mudahtest.repository.data.model.entity.ChatMessage
import com.example.mudahtest.util.DateFormatterUtils
import com.example.mudahtest.util.DateFormatterUtils.convertDateToSpecificFormat

class MessageListAdapter(private var mMessageList: List<ChatMessage>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_MESSAGE_SENT = 1
        private const val VIEW_TYPE_MESSAGE_RECEIVED = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_MESSAGE_SENT -> SentMessageHolder(
                ViewChatMessageOutboundBinding.inflate(layoutInflater, parent, false)
            )
            VIEW_TYPE_MESSAGE_RECEIVED -> ReceivedMessageHolder(
                ViewChatMessageInboundBinding.inflate(layoutInflater, parent, false)
            )
            else -> SentMessageHolder(
                ViewChatMessageOutboundBinding.inflate(layoutInflater, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_MESSAGE_SENT -> initiateHeaderItem(holder as SentMessageHolder, position)
            VIEW_TYPE_MESSAGE_RECEIVED -> initiateReceivedMMessageItem(
                holder as ReceivedMessageHolder,
                position
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message = mMessageList[position]
        return if (message.isOutBoundMessage) {
            VIEW_TYPE_MESSAGE_SENT
        } else {
            VIEW_TYPE_MESSAGE_RECEIVED
        }
    }

    override fun getItemCount(): Int {
        return mMessageList.size
    }

    class SentMessageHolder(viewBinding: ViewChatMessageOutboundBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        var binding: ViewChatMessageOutboundBinding = viewBinding
    }

    class ReceivedMessageHolder(viewBinding: ViewChatMessageInboundBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        var binding: ViewChatMessageInboundBinding = viewBinding
    }

    private fun initiateHeaderItem(viewHolder: SentMessageHolder, position: Int) {
        val message = mMessageList[position]
        message?.let {
            viewHolder.binding.tvMessage.text = message.message
            viewHolder.binding.tvTimeStamp.text = convertDateToSpecificFormat(
                message.timestamp, DateFormatterUtils.DATEFORMAT.SERVER_TIME_FORMAT,
                DateFormatterUtils.DATEFORMAT.HH_MM
            )
        }
    }

    private fun initiateReceivedMMessageItem(viewHolder: ReceivedMessageHolder, position: Int) {
        val message = mMessageList[position]
        message?.let {
            viewHolder.binding.tvMessage.text = message.message
            viewHolder.binding.tvTimeStamp.text = convertDateToSpecificFormat(
                message.timestamp, DateFormatterUtils.DATEFORMAT.SERVER_TIME_FORMAT,
                DateFormatterUtils.DATEFORMAT.HH_MM
            )
        }
    }

    fun submitList(messageList: List<ChatMessage>) {
        mMessageList = messageList
        notifyDataSetChanged()
    }

}