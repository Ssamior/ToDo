package com.example.todo.user

import com.example.todo.network.UserInfo
import okhttp3.MultipartBody

class UserInfoRepository {

    private val userWebService = Api.userWebService;

    suspend fun refresh(): UserInfo? {
        val response = userWebService.getInfo()
        if(response.isSuccessful) {
            return response.body()
        }
        return null;
    }

    suspend fun updateAvatar(avatar: MultipartBody.Part): UserInfo? {
        val response = userWebService.updateAvatar(avatar)
        if(response.isSuccessful) {
            return response.body()
        }
        return null;
    }

    suspend fun update(user: UserInfo): UserInfo? {
        val response = userWebService.update(user)
        if(response.isSuccessful) {
            return response.body()
        }
        return null;
    }

}