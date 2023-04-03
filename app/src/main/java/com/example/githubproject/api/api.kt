package com.example.githubproject.api

import com.example.githubproject.data.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface Api {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getSrcUser(
        @Query("q") query: String
    ): Call<UserResponse>
}