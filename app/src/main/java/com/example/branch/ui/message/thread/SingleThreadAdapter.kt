package com.example.branch.ui.message.thread

import android.text.Html
import android.text.Html.FROM_HTML_MODE_LEGACY
import android.text.Spanned
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.branch.R
import com.example.branch.databinding.ItemSingleMessageBinding
import com.example.branch.ui.message.Message
import com.example.branch.utils.DateTimeUtils

class SingleThreadAdapter :
    ListAdapter<Message, SingleThreadAdapter.ViewHolder>(DiffUtilCallBack()) {

    inner class ViewHolder(val binding: ItemSingleMessageBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSingleMessageBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        with(holder.binding) {
            tvBody.text = data.bodyMessage
            tvAgentId.apply {
                val myText: String = resources.getString(R.string.agent_id, data.agentId.toString())
                val styledText: Spanned = Html.fromHtml(myText, FROM_HTML_MODE_LEGACY)
                text = styledText.toString()
            }
            tvCreatedAt.text = DateTimeUtils.convertDateStringToRelativeTime(data.timeStamp)
        }
    }

    class DiffUtilCallBack : DiffUtil.ItemCallback<Message>() {

        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.timeStamp == newItem.timeStamp
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }

    }
}