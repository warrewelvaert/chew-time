package com.example.warre_welvaert_project_android.data

import com.example.warre_welvaert_project_android.model.Food
import kotlinx.coroutines.flow.Flow

class OfflineFoodRepository(private val foodEntityDao: FoodEntityDao) : FoodRepository {
    override fun getAllFoodStream(): Flow<List<Food>> = foodEntityDao.getAll()

    override fun getFoodStream(id: Int): Flow<Food> = foodEntityDao.getById(id)

    override suspend fun insertFood(food: Food) = foodEntityDao.insert(food)

    override suspend fun deleteFood(food: Food) = foodEntityDao.delete(food)

    override suspend fun updateFood(food: Food) = foodEntityDao.update(food)
}