package com.example.warre_welvaert_project_android.ui

import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.warre_welvaert_project_android.PetFoodApplication
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import com.example.warre_welvaert_project_android.ui.food.FoodDbViewModel
import com.example.warre_welvaert_project_android.ui.food.FoodApiViewModel
import com.example.warre_welvaert_project_android.ui.food.FoodDetailViewModel
import com.example.warre_welvaert_project_android.ui.pet.AddPetViewModel
import com.example.warre_welvaert_project_android.ui.pet.PetDbViewModel
import com.example.warre_welvaert_project_android.ui.timer.TimerViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {

        initializer {
            FoodDbViewModel(petFoodApplication().container.foodsRepository)
        }

        initializer {
            FoodApiViewModel()
        }

        initializer {
            PetDbViewModel(petFoodApplication().container.petsRepository)
        }

        initializer {
            AddPetViewModel()
        }

        initializer {
            TimerViewModel()
        }

        initializer {
            FoodDetailViewModel()
        }
    }
}

fun CreationExtras.petFoodApplication(): PetFoodApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as PetFoodApplication)
