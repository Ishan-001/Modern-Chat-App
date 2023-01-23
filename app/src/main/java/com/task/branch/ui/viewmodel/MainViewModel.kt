package com.task.branch.ui.viewmodel

import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.branch.model.LoginRequest
import com.task.branch.model.LoginResponse
import com.task.branch.model.Message
import com.task.branch.model.MessageRequest
import com.task.branch.repository.MainRepository
import com.task.branch.util.Constants.AUTH_TOKEN
import com.task.branch.util.Resource
import com.task.branch.util.Screen
import com.task.branch.util.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository : MainRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val chatsResponse = MutableLiveData<Resource<List<ArrayList<Message>>>>()
    private val authResponse = MutableLiveData<Resource<LoginResponse>>()
    private val messageResponse = MutableLiveData<Resource<Message>>()

    private val currentThread = MutableLiveData<List<Message>>()

    private val fragmentState = MutableLiveData<Screen>()

    fun login(username : String, password : String) = viewModelScope.launch {
        authResponse.postValue(Resource.Loading)
        try {
            repository.login(LoginRequest(username, password)).let {
                if (it.isSuccessful) {
                    authResponse.postValue(Resource.Success(it.body()))
                    sharedPreferences.edit().putString(AUTH_TOKEN, it.body()?.authToken).apply()
                }
                else
                    authResponse.postValue(Resource.Error(it.message()))
            }
        } catch (e: Exception) {
            authResponse.postValue(Resource.Exception(e))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAllMessages() = viewModelScope.launch {
        chatsResponse.postValue(Resource.Loading)
        try {
            repository.getMessages(sharedPreferences.getString(AUTH_TOKEN, "")!!).let {
                if (it.isSuccessful)
                    withContext(Dispatchers.Default) {
                        chatsResponse.postValue(Resource.Success(it.body()
                            ?.let { data -> Utils.getGroupedChats(data) }))
                    }
                else
                    chatsResponse.postValue(Resource.Error(it.message()))
            }
        } catch (e: Exception) {
            chatsResponse.postValue(Resource.Exception(e))
        }
    }

    fun sendMessage(threadId : Int, messageBody : String) = viewModelScope.launch {
        messageResponse.postValue(Resource.Loading)
        try {
            repository.addMessage(
                sharedPreferences.getString(AUTH_TOKEN, "")!!,
                MessageRequest(threadId, messageBody)
            ).let {
                if (it.isSuccessful)
                    messageResponse.postValue(Resource.Success(it.body()))
                else
                    messageResponse.postValue(Resource.Error(it.message()))
            }
        } catch (e: Exception) {
            messageResponse.postValue(Resource.Exception(e))
        }
    }

    fun getChatsResponse() : LiveData<Resource<List<ArrayList<Message>>>> = chatsResponse
    fun getAuthResponse() : LiveData<Resource<LoginResponse>> = authResponse
    fun getMessageResponse() : LiveData<Resource<Message>> = messageResponse
    fun getCurrentThread() : LiveData<List<Message>> = currentThread
    fun getFragmentState() : LiveData<Screen> = fragmentState

    fun setCurrentThread(thread : List<Message>) {
        currentThread.postValue(thread)
    }

    fun setFragmentState(screen : Screen) {
        fragmentState.postValue(screen)
    }
}