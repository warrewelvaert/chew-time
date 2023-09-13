package com.example.warre_welvaert_project_android.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalTime

@Entity(tableName = "Pet")
data class Pet (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val animal: String,
    @ColumnInfo(name = "feeding_times")
    val feedingTimes: List<LocalTime> = listOf(),
    @ColumnInfo(name = "image")
    val image: ByteArray? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Pet

        return id == other.id
    }
    override fun hashCode(): Int {
        return id
    }
}
