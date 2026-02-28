package com.jmlucero.alkewallet.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

   // private const val BASE_URL = "http://10.0.2.2:8000/"
    private const val BASE_URL = "https://alkewallet.stehenmed.cl/"
    //En el servidor local hay que correr:
    // php -S 0.0.0.0:8000 -t public

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val userService: UserService by lazy {
        retrofit.create(UserService::class.java)
    }
}