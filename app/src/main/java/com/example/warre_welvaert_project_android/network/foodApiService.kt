package com.example.warre_welvaert_project_android.network

import com.example.warre_welvaert_project_android.model.Food
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://nma-project-server.onrender.com/api/"

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface FoodApiService {
    @GET("cat/wet")
    suspend fun getWetCatFoods(): List<Food>

    @GET("cat/dry")
    suspend fun getDryCatFoods(): List<Food>

    @GET("dog/wet")
    suspend fun getWetDogFoods(): List<Food>

    @GET("dog/dry")
    suspend fun getDryDogFoods(): List<Food>
}

object FoodApi {
    val retrofitService: FoodApiService by lazy {
        retrofit.create(FoodApiService::class.java)
    }
}