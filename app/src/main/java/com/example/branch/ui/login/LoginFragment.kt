package com.example.branch.ui.login

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.example.branch.R
import com.example.branch.base.BaseActivity
import com.example.branch.base.BaseFragment
import com.example.branch.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment() {

    private lateinit var binding: FragmentLoginBinding

    private val viewModel by viewModel<LoginViewModel>()

    override fun initView(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initData(view: View) {

        viewModel.loading.observe(viewLifecycleOwner) { onLoading(it) }
        viewModel.response.observe(viewLifecycleOwner) { onResponse(it) }
        viewModel.error.observe(viewLifecycleOwner) { onError(it) }

        viewModel.isEnable.observe(viewLifecycleOwner) { onLoginButtonEnable(it) }
    }

    override fun initListener(view: View) {

        binding.tvUserName.doOnTextChanged { text, _, _, _ ->
            viewModel.setUnamePass(username = text.toString())
        }

        binding.tvPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.setUnamePass(password = text.toString())
        }

        binding.btLogin.setOnClickListener {
            if (!binding.tvUserName.text.isNullOrBlank() && !binding.tvPassword.text.isNullOrBlank()) {
                viewModel.login()
            }
        }
    }

    private fun onLoginButtonEnable(it: Boolean) {
        binding.btLogin.isEnabled = it
    }

    private fun onLoading(it: Boolean) {
        if (it) {
            (requireActivity() as BaseActivity).showLoadingDialog(true)
        } else {
            (requireActivity() as BaseActivity).showLoadingDialog(false)
        }
    }

    private fun onResponse(it: LoginResponse) {
        findNavController().navigate(R.id.action_loginFragment_to_messageFragment)
    }

    private fun onError(it: String) {
        (requireActivity() as BaseActivity).showLoadingDialog(false)
        Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG)
            .setAction("Retry") {
                viewModel.login()
            }.show()
    }
}
