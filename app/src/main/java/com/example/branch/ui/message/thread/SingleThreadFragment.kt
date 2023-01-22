package com.example.branch.ui.message.thread

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.branch.base.BaseFragment
import com.example.branch.databinding.FragmentSingleThreadBinding
import com.example.branch.ui.message.Message
import com.example.branch.ui.message.MessageViewModel
import com.example.branch.utils.showKeyboard
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class SingleThreadFragment : BaseFragment() {

    private lateinit var binding: FragmentSingleThreadBinding
    private val viewModel: MessageViewModel by activityViewModel()

    private val messageList = ArrayList<Message>()

    private lateinit var adapter: SingleThreadAdapter

    override fun initView(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = FragmentSingleThreadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initData(view: View) {
        adapter = SingleThreadAdapter()

        binding.rcvMessages.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
        binding.rcvMessages.adapter = adapter

        viewModel.threadData.observe(viewLifecycleOwner) { onResponse(it) }

        viewModel.sendLoading.observe(viewLifecycleOwner) { onSendMessageLoading(it) }
        viewModel.sendMessage.observe(viewLifecycleOwner) { onMessageSendRes(it) }
        viewModel.sendError.observe(viewLifecycleOwner) { onSendMessageError(it) }
    }

    override fun initListener(view: View) {

        binding.etMessage.doOnTextChanged { text, _, _, _ ->
            binding.btSend.isEnabled = !text.isNullOrEmpty()
        }

        binding.btSend.setOnClickListener {
            viewModel.sendMessage(binding.etMessage.text.toString())
        }

        binding.etMessage.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                binding.btSend.performClick()
                return@setOnEditorActionListener false
            }
            true
        }
    }

    private fun onResponse(it: List<Message>) {
        messageList.clear()
        messageList.addAll(it)
        adapter.submitList(messageList)
    }

    private fun onSendMessageLoading(it: Boolean?) {
        if (it == true) {
            binding.btSend.isEnabled = false
        } else if (it == false) {
            binding.btSend.isEnabled = true
        }
    }


    private fun onMessageSendRes(message: Message?) {
        message?.let {
            messageList.add(0, it)
            adapter.submitList(messageList)
            adapter.notifyItemInserted(0)
            binding.rcvMessages.scrollToPosition(0)
        }
        onSendMessageError(null)
    }


    private fun onSendMessageError(it: String?) {
        if (it == null) {
            binding.etMessage.setText("")
            binding.btSend.isEnabled = false
        } else {
            binding.btSend.isEnabled = true
            Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG)
                .setAnchorView(binding.etMessage)
                .setAction("Retry") {
                    viewModel.sendMessage(binding.etMessage.text.toString())
                }.show()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.etMessage.requestFocus()
        binding.etMessage.showKeyboard()
    }

}