package com.amsh.app.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val token: String, val userId: String)
data class Item(val id: String, val title: String, val subtitle: String)

interface ApiService {
    @POST("auth/login")
    fun login(@Body req: LoginRequest): Call<LoginResponse>

    @GET("items")
    fun getItems(): Call<List<Item>>
}
