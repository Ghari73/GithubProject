package com.example.githubproject.usif.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubproject.api.ApiConfig
import com.example.githubproject.data.model.UserDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel: ViewModel() {
    val user = MutableLiveData<UserDetailResponse>()
    private val isLoading = MutableLiveData<Boolean>()

    fun setUserDetail(username: String){
        isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                isLoading.value = false
                if (response.isSuccessful){
                    user.postValue(response.body())
                }else{
                    Log.e("UserViewModel","Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                isLoading.value = false
                Log.d("Fail",t.message.toString())
                t.printStackTrace()
            }

        })

    }

    fun getUserDetail(): LiveData<UserDetailResponse>{
        return user
    }

    fun isLoad(): LiveData<Boolean> {
        return isLoading
    }


}