package com.example.githubuser.data.retrofit

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object{
        fun getApiService(): ApiService{
            val authInterceptor = Interceptor {chain ->
                val req = chain.request()
                val reqHeaders = req.newBuilder()
                    .addHeader("Authorization", "12345")
                    .build()
                chain.proceed(reqHeaders)
            }

            val client =OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .build()

            val retrofit=Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}