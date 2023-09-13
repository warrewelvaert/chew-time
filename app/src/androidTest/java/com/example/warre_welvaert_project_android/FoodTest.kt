package com.example.warre_welvaert_project_android

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.warre_welvaert_project_android.model.Food
import com.example.warre_welvaert_project_android.ui.food.FoodDetailViewModel
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
class FoodTest {
    private val viewModel = FoodDetailViewModel()
    private val foodOne = Food(
        name = "Cat Food",
        animal = "cat",
        amount = 2000,
        dailyConsumption = 200,
        storeUrl = "example.com",
        type = "dry",
        dateCreated = LocalDateTime.now().minusDays(2)
    )
    private val foodTwo = Food(
        name = "Cat Food",
        animal = "cat",
        amount = 2000,
        dailyConsumption = 0,
        storeUrl = "example.com",
        type = "dry",
    )
    private val foodThree = Food(
        name = "Cat Food",
        animal = "cat",
        amount = 0,
        dailyConsumption = 10,
        storeUrl = "example.com",
        type = "dry",
    )

    @Test
    fun foodAmountTest() {
        Assert.assertEquals(1600, viewModel.getAmountLeft(foodOne))
        Assert.assertEquals(2000, viewModel.getAmountLeft(foodTwo))
        Assert.assertEquals(0, viewModel.getAmountLeft(foodThree))
    }

    @Test
    fun foodDaysTest() {
        Assert.assertEquals(8, viewModel.getDaysLeft(foodOne))
        Assert.assertEquals(0, viewModel.getDaysLeft(foodTwo))
        Assert.assertEquals(0, viewModel.getDaysLeft(foodThree))
    }
}