package com.limestudio.findlottery.data.network

import com.limestudio.findlottery.data.models.Ticket
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("api/Authentication/RequestToken")
    suspend fun signIn(@Body body: String): Ticket

    @GET("api/Authentication/RequestToken")
    suspend fun getFeed(): ArrayList<Ticket>
}