package com.limestudio.findlottery.data.network

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkManager {
    val webservice by lazy {
        Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build().create(ApiService::class.java)
    }
}