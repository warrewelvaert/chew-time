package com.example.warre_welvaert_project_android.data

import com.example.warre_welvaert_project_android.model.Pet
import kotlinx.coroutines.flow.Flow

interface PetRepository {
    fun getAllPetStream(): Flow<List<Pet>>

    fun getPetStream(id: Int): Flow<Pet>

    suspend fun insertPet(pet: Pet)

    suspend fun deletePet(pet: Pet)

    suspend fun updatePet(pet: Pet)
}