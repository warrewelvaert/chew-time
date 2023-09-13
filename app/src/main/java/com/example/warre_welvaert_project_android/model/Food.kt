package com.example.warre_welvaert_project_android.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
@Entity(tableName = "Food")
data class Food(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "store_url")
    val storeUrl: String,
    val type: String,
    val animal: String,
    val amount: Int,
    @ColumnInfo(name = "daily_consumption")
    val dailyConsumption: Int,
    @ColumnInfo(name = "date_created")
    @Contextual
    val dateCreated: LocalDateTime = LocalDateTime.now()
)