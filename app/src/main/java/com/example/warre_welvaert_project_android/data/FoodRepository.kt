package com.example.warre_welvaert_project_android.data

import com.example.warre_welvaert_project_android.model.Food
import kotlinx.coroutines.flow.Flow

interface FoodRepository {

    fun getAllFoodStream(): Flow<List<Food>>

    fun getFoodStream(id: Int): Flow<Food>

    suspend fun insertFood(food: Food)

    suspend fun deleteFood(food: Food)

    suspend fun updateFood(food: Food)
}