package com.example.warre_welvaert_project_android.ui.pet

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.warre_welvaert_project_android.data.PetRepository
import com.example.warre_welvaert_project_android.model.Pet
import kotlinx.coroutines.flow.Flow

class PetDbViewModel(private val petRepository: PetRepository): ViewModel() {

    suspend fun savePet(pet: Pet) {
        if (isValid(pet = pet)) {
            petRepository.insertPet(pet = pet)
        } else Log.e("Pet Db", "Failed to add pet to Database because Pet object was not valid.")
    }

    fun getAllPets(): Flow<List<Pet>> {
        return petRepository.getAllPetStream()
    }

    fun getById(id: Int?): Flow<Pet> {
        val petId: Int = id!!
        return petRepository.getPetStream(id = petId)
    }

    suspend fun update(pet: Pet) {
        if (isValid(pet = pet)) {
            petRepository.updatePet(pet = pet)
        } else Log.e("Pet Db", "Failed to update pet in the Database because Pet object was not valid.")
    }

    suspend fun delete(pet: Pet) {
        petRepository.deletePet(pet = pet)
    }

    private fun isValid(pet: Pet): Boolean {
        return !(pet.name.isBlank() || pet.animal.isBlank())
    }
}