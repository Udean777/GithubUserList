package com.example.githubuser.data.retrofit

import com.example.githubuser.data.response.ResponseDetail
import com.example.githubuser.data.response.ResponseFollowersItem
import com.example.githubuser.data.response.ResponseSearch
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    fun searchUsers(@Query("q") query: String): Call<ResponseSearch>

    @GET("users/{username}")
    fun getDetailUsers(@Path("username") username: String): Call<ResponseDetail>

    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String): Call<List<ResponseFollowersItem>>

    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String): Call<List<ResponseFollowersItem>>
}