package com.example.branch.ui.message

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.branch.network.ApiMethods
import com.example.branch.utils.PrefKeys
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MessageViewModel(
    private val apiMethods: ApiMethods, sharedPreferences: SharedPreferences
) : ViewModel() {

    companion object {
        private const val TAG: String = "MessageViewModel"
    }

    private val authToken = sharedPreferences.getString(PrefKeys.AUTH_TOKEN, null)!!

    private var selectedThread: Int? = null

    private var _response: MutableLiveData<List<Message>> = MutableLiveData()
    private var _loading: MutableLiveData<Boolean> = MutableLiveData()
    private var _error: MutableLiveData<String> = MutableLiveData()

    private val response: LiveData<List<Message>> = _response
    val loading: LiveData<Boolean> = _loading
    val error: LiveData<String> = _error

    private var _responseDistinct: MutableLiveData<List<Message>> = MutableLiveData()
    val responseDistinct: LiveData<List<Message>> = _responseDistinct

    private var _threadData: MutableLiveData<List<Message>> = MutableLiveData()
    val threadData: LiveData<List<Message>> = _threadData

    private var _sendResponse: MutableLiveData<Message?> = MutableLiveData()
    private var _sendLoading: MutableLiveData<Boolean?> = MutableLiveData()
    private var _sendError: MutableLiveData<String?> = MutableLiveData()

    val sendMessage: LiveData<Message?> = _sendResponse
    val sendLoading: LiveData<Boolean?> = _sendLoading
    val sendError: LiveData<String?> = _sendError

    fun getAllMessages() {
        viewModelScope.launch {
            runCatching {
                if (_responseDistinct.value == null) {
                    emitUIAllMessages(showProgress = true)
                }
                apiMethods.getMessages(authToken)
            }.onSuccess { listResponse ->
                emitUIAllMessages(showProgress = false)
                if (listResponse.isSuccessful) {
                    if (!listResponse.body().isNullOrEmpty()) {
                        emitUIAllMessages(response = listResponse.body())
                    } else {
                        emitUIAllMessages(error = "Please Try Again.....")
                    }
                } else {
                    emitUIAllMessages(error = listResponse.message())
                }
            }.onFailure {
                emitUIAllMessages(showProgress = false, error = it.localizedMessage)
            }
        }
    }

    private fun emitUIAllMessages(
        showProgress: Boolean? = false, response: List<Message>? = null, error: String? = null
    ) {
        if (showProgress != null) _loading.postValue(showProgress)
        if (response != null) {
            val sortedResponse = sortByTimestamp(response)
            _response.postValue(sortedResponse)
            _responseDistinct.postValue(sortedResponse.distinctBy {
                it.threadId
            })
        }
        if (error != null) _error.postValue(error)
    }

    fun selectedThread(threadId: Int) {
        selectedThread = threadId
        _threadData.postValue(response.value?.filter {
            it.threadId == threadId
        })
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            runCatching {
                emitUIMessages(showProgress = true)
                val sendMessage = SendMessage(selectedThread!!, message)
                apiMethods.sendMessage(sendMessage, authToken)
            }.onSuccess {
                emitUIMessages(showProgress = false)
                if (it.isSuccessful) {
                    if (it.body() != null) {
                        emitUIMessages(response = it.body())
                        getAllMessages()
                    } else {
                        emitUIMessages(error = "Please retry..")
                    }
                } else {
                    emitUIMessages(error = it.message())
                }
            }.onFailure {
                emitUIMessages(showProgress = false, error = it.localizedMessage)
            }
        }
    }

    private fun emitUIMessages(
        showProgress: Boolean? = false, response: Message? = null, error: String? = null
    ) {
        if (showProgress != null) _sendLoading.postValue(showProgress)
        if (response != null) {
            _sendResponse.postValue(response)
        }
        if (error != null) _sendError.postValue(error)
    }

    private fun sortByTimestamp(list: List<Message>): List<Message> {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        return list.sortedByDescending {
            val itemDate = inputFormat.parse(it.timeStamp)
            itemDate?.time
        }
    }

    fun reset() {
        _sendResponse.value = null
        _sendLoading.value = null
        _sendError.value = null
    }

}