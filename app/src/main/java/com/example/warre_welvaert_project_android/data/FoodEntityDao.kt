package com.example.warre_welvaert_project_android.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.warre_welvaert_project_android.model.Food
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodEntityDao {
    @Query(
        """
        SELECT * FROM food 
        ORDER BY name ASC    
        """
    )
    fun getAll(): Flow<List<Food>>

    @Query(
        """
        SELECT * FROM food 
        WHERE id = :id 
        """
    )
    fun getById(id: Int): Flow<Food>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(food: Food)

    @Update
    suspend fun update(food: Food)

    @Delete
    suspend fun delete(food: Food)

}