package com.example.githubproject.usif.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubproject.api.ApiConfig
import com.example.githubproject.data.model.ItemsItem
import com.example.githubproject.data.model.UserResponse
import com.example.githubproject.repository.FavUserRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel(private val repo: FavUserRepository) : ViewModel() {
    fun SearchUsersSetter(query: String){
        viewModelScope.launch {
            repo.getUser(query)
        }
    }

    fun getSrcUser(): LiveData<List<ItemsItem>>{
        return repo.user
    }


    fun isLoad(): LiveData<Boolean>{
        return repo.isLoading
    }
}