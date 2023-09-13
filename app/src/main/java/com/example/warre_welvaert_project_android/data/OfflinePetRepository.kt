package com.example.warre_welvaert_project_android.data

import com.example.warre_welvaert_project_android.model.Pet
import kotlinx.coroutines.flow.Flow

class OfflinePetRepository(private val petEntityDao: PetEntityDao): PetRepository {

    override fun getAllPetStream(): Flow<List<Pet>> = petEntityDao.getAll()

    override fun getPetStream(id: Int): Flow<Pet> = petEntityDao.getById(id)

    override suspend fun insertPet(pet: Pet) = petEntityDao.insert(pet)

    override suspend fun deletePet(pet: Pet) = petEntityDao.delete(pet)

    override suspend fun updatePet(pet: Pet) = petEntityDao.update(pet)
}