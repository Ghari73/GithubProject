package com.example.githubproject.api

import com.example.githubproject.BuildConfig
import com.example.githubproject.data.model.ItemsItem
import com.example.githubproject.data.model.UserDetailResponse
import com.example.githubproject.data.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {


    @GET("search/users")
    @Headers("Authorization: token ${BuildConfig.KEY}")
    fun getSrcUser(
        @Query("q") query: String
    ): Call<UserResponse>

    @GET("users/{username}")
    @Headers("Authorization: token ${BuildConfig.KEY}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<UserDetailResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token ${BuildConfig.KEY}")
    fun getFollowers(
        @Path("username") username: String
    ): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    @Headers("Authorization: token ${BuildConfig.KEY}")
    fun getFollowing(
        @Path("username") username: String
    ): Call<List<ItemsItem>>
}