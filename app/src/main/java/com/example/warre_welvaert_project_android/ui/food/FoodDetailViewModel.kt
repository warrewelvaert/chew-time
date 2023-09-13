package com.example.warre_welvaert_project_android.ui.food

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.warre_welvaert_project_android.model.Food
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.NumberFormatException
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

class FoodDetailViewModel: ViewModel() {

    fun getAmountLeft(food: Food): Long {
        val daysPassed = ChronoUnit.DAYS.between(food.dateCreated, LocalDateTime.now())
        return food.amount - food.dailyConsumption * daysPassed
    }

    fun getDaysLeft(food: Food): Long {
        if (food.amount <= 0 || food.dailyConsumption <= 0) {
            return 0
        }
        return getAmountLeft(food = food) / food.dailyConsumption
    }

    private fun getNotificationTriggerTime(food: Food): Long {
        if (getDaysLeft(food = food) < 2) {
            return 0
        }
        return getDaysLeft(food = food) - 2
    }

    fun updateFoodInDb (
        dbViewModel: FoodDbViewModel,
        food: Food,
        foodAmount: String,
        scope: CoroutineScope,
        ctx: Context
    ) {
        scope.launch {
            try {
                val parsedAmount = foodAmount.toInt()
                if (parsedAmount <= 0) {
                    throw NumberFormatException()
                }
                val newFood = Food(
                    id = food.id,
                    amount = getAmountLeft(food = food).toInt() + parsedAmount,
                    name = food.name,
                    type = food.type,
                    animal = food.animal,
                    dailyConsumption = food.dailyConsumption,
                    storeUrl = food.storeUrl,
                    dateCreated = LocalDateTime.now()
                )
                dbViewModel.update(food = newFood)
                scheduleNotification(ctx = ctx, triggerTimeDays = getNotificationTriggerTime(newFood), food = newFood)
                Toast.makeText(ctx, "Added ${parsedAmount}g of food.", Toast.LENGTH_LONG).show()
            } catch (e: NumberFormatException) {
                Log.e("DB Error", "Failed to parse food amount.")
            } catch (e: Exception) {
                Log.e("DB Error", "Failed to update entry in food database.")
            }
        }
    }

    fun addFoodToDb(
        food: Food,
        amount: String,
        consumption: String,
        dbViewModel: FoodDbViewModel,
        ctx: Context
    ): Boolean {
        try {
            if (amount.toInt() <= 0 || consumption.toInt() <= 0 || amount.isBlank() || consumption.isBlank()) {
                throw NumberFormatException()
            }
            val newFood = Food(
                name = food.name,
                animal = food.animal,
                type = food.type,
                amount = amount.toInt(),
                dailyConsumption = consumption.toInt(),
                storeUrl = food.storeUrl,
                dateCreated = LocalDateTime.now()
            )
            runBlocking {
                dbViewModel.saveFood(food = newFood)
            }
            scheduleNotification(ctx = ctx, triggerTimeDays = getNotificationTriggerTime(newFood), food = newFood)
            return true
        } catch (e: NumberFormatException) {
            Toast.makeText(ctx, "Food amount and consumption must be valid numbers.", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Log.e("DB Error", "Something went wrong while adding a food to the Database.")
        }
        return false
    }

    private fun scheduleNotification(ctx: Context, triggerTimeDays: Long, food: Food) {
        val inputData = Data.Builder()
            .putString("food_id", food.id.toString())
            .putString("food_name", food.name)
            .build()

        val notificationWork = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInputData(inputData)
            .setInitialDelay(triggerTimeDays, TimeUnit.DAYS)
            .build()

        WorkManager.getInstance(ctx).enqueueUniqueWork(
            "notification_work_${food.id}",
            ExistingWorkPolicy.REPLACE,
            notificationWork
        )
    }
}