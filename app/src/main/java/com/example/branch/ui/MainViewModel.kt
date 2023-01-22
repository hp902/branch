package com.example.branch.ui

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.example.branch.utils.PrefKeys.AUTH_TOKEN

class MainViewModel(private val sharedPreferences: SharedPreferences) : ViewModel() {

    fun isUserLoggedIn() = !sharedPreferences.getString(AUTH_TOKEN, null).isNullOrEmpty()

}