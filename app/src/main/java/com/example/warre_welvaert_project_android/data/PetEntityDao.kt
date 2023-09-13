package com.example.warre_welvaert_project_android.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.warre_welvaert_project_android.model.Pet
import kotlinx.coroutines.flow.Flow

@Dao
interface PetEntityDao {
    @Query(
        """
        SELECT * FROM pet 
        ORDER BY name ASC    
        """
    )
    fun getAll(): Flow<List<Pet>>

    @Query(
        """
        SELECT * FROM pet 
        WHERE id = :id 
        """
    )
    fun getById(id: Int): Flow<Pet>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(pet: Pet)

    @Update
    suspend fun update(pet: Pet)

    @Delete
    suspend fun delete(pet: Pet)
}