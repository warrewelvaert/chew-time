package com.example.warre_welvaert_project_android.ui.food

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.warre_welvaert_project_android.model.Food
import com.example.warre_welvaert_project_android.network.FoodApi
import kotlinx.coroutines.launch

sealed interface FoodUiState {
    data class Success(
        val dryCatFoods: List<Food>,
        val wetCatFoods: List<Food>,
        val dryDogFoods: List<Food>,
        val wetDogFoods: List<Food>,
    ) : FoodUiState
    data object Error : FoodUiState
    data object Loading : FoodUiState
}

class FoodApiViewModel: ViewModel() {
    var foodUiState: FoodUiState by mutableStateOf(FoodUiState.Loading)
        private set

    init {
        fetchFood()
    }

    private fun fetchFood() {
        viewModelScope.launch {
            foodUiState = FoodUiState.Loading
            foodUiState = try {
                FoodUiState.Success(
                    wetCatFoods = FoodApi.retrofitService.getWetCatFoods(),
                    dryCatFoods = FoodApi.retrofitService.getDryCatFoods(),
                    wetDogFoods = FoodApi.retrofitService.getWetDogFoods(),
                    dryDogFoods = FoodApi.retrofitService.getDryDogFoods()
                )
            } catch (e: Exception) {
                Log.e("Food API", "Exception: : $e")
                FoodUiState.Error
            }
        }
    }
}