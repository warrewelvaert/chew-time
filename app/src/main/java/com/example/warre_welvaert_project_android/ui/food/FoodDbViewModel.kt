package com.example.warre_welvaert_project_android.ui.food

import androidx.lifecycle.ViewModel
import com.example.warre_welvaert_project_android.data.FoodRepository
import com.example.warre_welvaert_project_android.model.Food
import android.util.Log
import kotlinx.coroutines.flow.Flow

class FoodDbViewModel(private val foodRepository: FoodRepository): ViewModel() {

    suspend fun saveFood(food: Food) {
        if (isValid(food)) {
            foodRepository.insertFood(food = food)
        } else Log.e("Food Db", "Failed to add food to Database because food object was not valid.")
    }

    fun getAllFoods(): Flow<List<Food>> {
        return foodRepository.getAllFoodStream()
    }

    fun getById(id: Int?): Flow<Food> {
        val foodId: Int = id!!
        return foodRepository.getFoodStream(id = foodId)
    }

    suspend fun update(food: Food) {
        if (isValid(food)) {
            foodRepository.updateFood(food = food)
        } else Log.e("Food Db", "Failed to update food in Database because food object was not valid.")
    }

    suspend fun delete(food: Food) {
        foodRepository.deleteFood(food = food)
    }

    private fun isValid(food: Food): Boolean {
        return !(food.name.isBlank() || food.animal.isBlank()
                || food.amount < 0 || food.dailyConsumption < 0
                || food.type.isBlank() || food.storeUrl.isBlank())
    }
}