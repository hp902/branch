package com.example.branch.ui.login

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.branch.network.ApiMethods
import com.example.branch.utils.PrefKeys
import com.google.gson.Gson
import kotlinx.coroutines.launch

class LoginViewModel(
    private val apiMethods: ApiMethods,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    companion object {
        private const val TAG: String = "LoginViewModel"
    }

    private var _response: MutableLiveData<LoginResponse> = MutableLiveData()
    private var _loading: MutableLiveData<Boolean> = MutableLiveData()
    private var _error: MutableLiveData<String> = MutableLiveData()

    val response: LiveData<LoginResponse> = _response
    val loading: LiveData<Boolean> = _loading
    val error: LiveData<String> = _error

    private var _isEnable: MutableLiveData<Boolean> = MutableLiveData()
    val isEnable: LiveData<Boolean> = _isEnable

    var userName: String = ""
    var password: String = ""

    fun login() {
        viewModelScope.launch {
            runCatching {
                emitUIWatch(showProgress = true)
                apiMethods.login(LoginModel(userName, password))
            }.onSuccess {
                emitUIWatch(showProgress = false)
                if (it.isSuccessful) {
                    if (it.body()?.token != null) {
                        emitUIWatch(response = it.body())
                        addTokenToPref(it.body())
                    } else {
                        emitUIWatch(error = "Please try again")
                    }
                } else {
                    val message =
                        Gson().fromJson(it.errorBody()!!.charStream(), LoginError::class.java)
                    emitUIWatch(error = message.error)
                }
            }.onFailure {
                emitUIWatch(showProgress = false, error = it.localizedMessage)
            }
        }
    }

    fun setUnamePass(username: String? = null, password: String? = null) {
        username?.let { this.userName = it }
        password?.let { this.password = it }
        checkForLoginButton()
    }


    private fun checkForLoginButton() {
        if (userName.isEmpty()) {
            _isEnable.postValue(false)
        } else if (password.isEmpty()) {
            _isEnable.postValue(false)
        }else {
            _isEnable.postValue(true)
        }
    }

    private fun emitUIWatch(
        showProgress: Boolean? = false,
        response: LoginResponse? = null,
        error: String? = null
    ) {
        if (showProgress != null) _loading.postValue(showProgress)
        if (response != null && _response.value == null) _response.postValue(response)
        if (error != null) _error.postValue(error)
    }

    private fun addTokenToPref(body: LoginResponse?) {
        sharedPreferences.edit().putString(PrefKeys.AUTH_TOKEN, body?.token).apply()
    }

}