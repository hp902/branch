package com.example.branch.ui

import androidx.navigation.fragment.NavHostFragment
import com.example.branch.R
import com.example.branch.base.BaseActivity
import com.example.branch.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModel<MainViewModel>()

    override fun initView() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initData() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.nav_graph)

        if (viewModel.isUserLoggedIn()) {
            graph.setStartDestination(R.id.messageFragment)
        } else {
            graph.setStartDestination(R.id.loginFragment)
        }

        val navController = navHostFragment.navController
        navController.setGraph(graph, intent.extras)
    }

    override fun initListener() {

    }
}