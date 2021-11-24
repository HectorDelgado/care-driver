package com.hectordelgado.caredrivers.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

object RetrofitBuilder {
    private const val BASE_URL = "https://storage.googleapis.com/hsd-interview-resources/"

    val apiService = getRetrofit().create(ApiService::class.java)

    private fun getRetrofit(): Retrofit {
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }
}