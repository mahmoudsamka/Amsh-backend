package com.amsh.app.network

import com.amsh.app.MyApp
import okhttp3.Interceptor
import okhttp3.Response
import android.content.Context

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val prefs = MyApp.instance.getSharedPreferences("amsh_prefs", Context.MODE_PRIVATE)
        val token = prefs.getString("auth_token", null)
        val request = if (!token.isNullOrEmpty()) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            chain.request()
        }
        return chain.proceed(request)
    }
}
