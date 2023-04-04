package com.example.githubproject.usif.main

import android.content.ClipData.Item
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubproject.api.ApiConfig
import com.example.githubproject.data.model.ItemsItem
import com.example.githubproject.data.model.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel:ViewModel() {

    val listUser = MutableLiveData<List<ItemsItem>>()
    val _listUser : LiveData<List<ItemsItem>> = listUser

    val isLoading = MutableLiveData<Boolean>()
    val _isLoading : LiveData<Boolean> = isLoading

    fun SearchUsersSetter(query: String){
        isLoading.value = true
        val client = ApiConfig.getApiService().getSrcUser(query)
        client.enqueue(object : Callback<UserResponse>{
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                isLoading.value = false
                if (response.isSuccessful){
                    listUser.value = response.body()?.items
                    println("panjang livedta : "+_listUser.value?.size)

//                    listUser.postValue(response.body()?.items)
                }else{
                    Log.e("UserViewModel","Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                isLoading.value = false
                Log.d("Fail",t.message.toString())
                t.printStackTrace()
            }

        })
    }

    fun getSrcUser(): LiveData<List<ItemsItem>>{
        return listUser
    }


    fun isLoad(): LiveData<Boolean>{
        return isLoading
    }
}