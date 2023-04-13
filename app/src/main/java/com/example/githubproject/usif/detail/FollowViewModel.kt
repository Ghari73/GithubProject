package com.example.githubproject.usif.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubproject.api.ApiConfig
import com.example.githubproject.data.model.ItemsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel: ViewModel() {
    val listFollowing = MutableLiveData<List<ItemsItem>>()
    val listFollowers = MutableLiveData<List<ItemsItem>>()
    private val isLoading = MutableLiveData<Boolean>()

    fun setFollowing(username: String){
        isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<ItemsItem>>{
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                isLoading.value = false
                if (response.isSuccessful){
                    listFollowing.postValue(response.body())
                }else{
                    Log.e("FollowingViewModel","Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                isLoading.value = false
                Log.d("Fail",t.message.toString())
                t.printStackTrace()
            }

        })

    }

    fun getFollowing(): LiveData<List<ItemsItem>>{
        return listFollowing
    }

    fun setFollowers(username: String){
        isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<ItemsItem>>{
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                isLoading.value = false
                if (response.isSuccessful){
                    listFollowers.postValue(response.body())
                }else{
                    Log.e("FollowersViewModel","Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                isLoading.value = false
                Log.d("Fail",t.message.toString())
                t.printStackTrace()
            }

        })

    }

    fun getFollowers(): LiveData<List<ItemsItem>>{
        return listFollowers
    }

    fun isLoad(): LiveData<Boolean> {
        return isLoading
    }
}