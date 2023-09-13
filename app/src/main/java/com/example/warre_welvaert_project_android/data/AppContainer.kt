package com.example.warre_welvaert_project_android.data

import android.content.Context

interface AppContainer {
    val foodsRepository: FoodRepository
    val petsRepository: PetRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val foodsRepository: FoodRepository by lazy {
        OfflineFoodRepository(AppDatabase.getDatabase(context).foodDao())
    }

    override val petsRepository: PetRepository  by lazy {
        OfflinePetRepository(AppDatabase.getDatabase(context).petDao())
    }
}