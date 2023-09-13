package com.example.warre_welvaert_project_android.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.warre_welvaert_project_android.model.Food
import com.example.warre_welvaert_project_android.model.Pet

@Database(entities = [Food::class, Pet::class], version = 9, exportSchema = false)
@TypeConverters(DbTypeConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun foodDao(): FoodEntityDao
    abstract fun petDao(): PetEntityDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
                    .build().also { Instance = it }
            }
        }
    }
}