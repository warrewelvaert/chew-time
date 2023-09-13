package com.example.warre_welvaert_project_android

import android.app.Application
import com.example.warre_welvaert_project_android.data.AppContainer
import com.example.warre_welvaert_project_android.data.AppDataContainer

class PetFoodApplication: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }

}