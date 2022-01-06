package com.example.todo.user

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.network.UserInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class UserInfoViewModel: ViewModel() {

    private val repository = UserInfoRepository();

    private var _userInfo = MutableStateFlow<UserInfo?>(null);
    var userInfo = _userInfo.asStateFlow() ;

    fun refresh() {
        viewModelScope.launch {
            val data = repository.refresh()
            if(data != null) {
                _userInfo.value = data
                userInfo = _userInfo;
            }
        }
    }

    fun updateAvatar(avatar: MultipartBody.Part) {
        viewModelScope.launch {
            val data = repository.updateAvatar(avatar)
            if(data != null) {
                _userInfo.value = data
                userInfo = _userInfo
            }
        }
    }

    fun update(user: UserInfo) {
        viewModelScope.launch {
            val data = repository.update(user)
            if(data != null) {
                _userInfo.value = data
                userInfo = _userInfo
            }
        }
    }

}