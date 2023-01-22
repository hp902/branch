package com.example.branch.ui.message

import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.branch.R
import com.example.branch.databinding.ItemMessageBinding
import com.example.branch.utils.DateTimeUtils

class MessageAdapter(
    private var onItemClicked: ((message: Message) -> Unit)
) : ListAdapter<Message, MessageAdapter.ViewHolder>(DiffUtilCallBack()) {

    inner class ViewHolder(val binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMessageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        with(holder.binding) {

            tvBody.apply {
                val myText: String = resources.getString(R.string.message_body, data.bodyMessage)
                val styledText: Spanned = Html.fromHtml(myText, Html.FROM_HTML_MODE_LEGACY)
                text = styledText
            }

            tvUsedID.apply {
                val myText: String = resources.getString(R.string.user_id, data.userId)
                val styledText: Spanned = Html.fromHtml(myText, Html.FROM_HTML_MODE_LEGACY)
                text = styledText
            }

            tvCreatedAt.apply {
                val myText: String = resources.getString(
                    R.string.created_at,
                    DateTimeUtils.convertDate(data.timeStamp)
                )
                val styledText: Spanned = Html.fromHtml(myText, Html.FROM_HTML_MODE_LEGACY)
                text = styledText
            }

            clMessContainer.setOnClickListener {
                onItemClicked(data)
            }
        }
    }

    class DiffUtilCallBack : DiffUtil.ItemCallback<Message>() {

        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.threadId == newItem.threadId && oldItem.timeStamp == newItem.timeStamp
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }

    }
}