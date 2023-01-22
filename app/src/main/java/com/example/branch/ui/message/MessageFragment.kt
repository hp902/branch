package com.example.branch.ui.message

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.branch.R
import com.example.branch.base.BaseActivity
import com.example.branch.base.BaseFragment
import com.example.branch.databinding.FragmentMessageBinding
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class MessageFragment : BaseFragment() {

    private lateinit var binding: FragmentMessageBinding
    private lateinit var adapter: MessageAdapter

    private val viewModel: MessageViewModel by activityViewModel()

    override fun initView(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = FragmentMessageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initData(view: View) {
        adapter = MessageAdapter {
            onThreadClicked(it)
        }

        binding.rcvMessages.layoutManager = LinearLayoutManager(requireContext())
        binding.rcvMessages.adapter = adapter

        viewModel.getAllMessages()

        viewModel.loading.observe(viewLifecycleOwner) { onLoading(it) }
        viewModel.responseDistinct.observe(viewLifecycleOwner) { onResponse(it) }
        viewModel.error.observe(viewLifecycleOwner) { onError(it) }
    }

    override fun initListener(view: View) {

    }

    private fun onLoading(it: Boolean) {
        if (it) {
            (requireActivity() as BaseActivity).showLoadingDialog(true)
        } else {
            (requireActivity() as BaseActivity).showLoadingDialog(false)
        }
    }


    private fun onResponse(it: List<Message>) {
        adapter.submitList(it)
    }

    private fun onError(it: String) {
        (requireActivity() as BaseActivity).showLoadingDialog(false)
        Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG)
            .setAction("Retry") {
                viewModel.getAllMessages()
            }.show()
    }

    private fun onThreadClicked(it: Message) {
        viewModel.reset()
        viewModel.selectedThread(it.threadId)
        findNavController().navigate(R.id.action_messageFragment_to_singleThreadFragment)
    }
}
