package com.example.warre_welvaert_project_android.ui.pet

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.warre_welvaert_project_android.model.Pet
import java.io.ByteArrayOutputStream
import java.time.LocalTime
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField
import java.time.Duration

class AddPetViewModel: ViewModel() {

    private val timeSlots = mutableStateListOf<LocalTime>()
    private var bitmap: Bitmap? = null

    fun addTimeSlot(timeSlot: LocalTime) {
        timeSlots.add(timeSlot)
    }

    fun removeTimeSlot(index: Int) {
        try {
            timeSlots.removeAt(index)
        } catch (e: IndexOutOfBoundsException) {
           Log.e("TimeSlot Error", "Failed to remove timeslot because index was not valid.")
        }
    }

    fun getAllTimeSlots(): List<LocalTime> {
        return timeSlots
    }

    fun getTimeSlot(id: Int): LocalTime? {
        return try {
            timeSlots[id]
        } catch (e: IndexOutOfBoundsException) {
            null
        }
    }

    fun updateTimeSlot(id: Int, newSlot: LocalTime) {
         try {
            timeSlots[id] = newSlot
        } catch (e: IndexOutOfBoundsException) {
             Log.e("TimeSlot Error", "Failed to update timeslot because index was not valid.")
        }
    }

    fun setImage(btm: Bitmap?) {
        bitmap = btm
    }

    fun getImage(): ByteArray? {
        val stream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 20, stream)
        return stream.toByteArray()
    }

    fun convertIntToTime(hour: Int, minute: Int): LocalTime {
        val timeFormatter = DateTimeFormatterBuilder()
            .appendValue(ChronoField.HOUR_OF_DAY, 2)
            .appendLiteral(':')
            .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
            .toFormatter()
        val timeString = String.format("%02d:%02d", hour, minute)
        return try {
            LocalTime.parse(timeString, timeFormatter)
        } catch (e: Exception) {
            Log.e("Parse Error", "Failed to parse timeslot.")
            LocalTime.NOON
        }
    }

    private fun calculateTimeDifference(time: LocalTime?): Duration? {
        if (time == null) {
            return Duration.ZERO
        }
        val current = LocalTime.now()
        val duration = Duration.between(current, time)
        if (duration.isNegative) {
            return duration.plusDays(1)
        }
        return duration
    }

    fun getSoonestTime(pet: Pet): String {
        if (pet.feedingTimes.isEmpty()) {
            return "??:??"
        }
        var soonestTime: LocalTime? = null
        var shortestDuration: Duration? = null

        for (feedingTime in pet.feedingTimes) {
            val duration: Duration? = calculateTimeDifference(feedingTime)
            if (shortestDuration == null || duration!! < shortestDuration) {
                shortestDuration = duration
                soonestTime = feedingTime
            }
        }
        return soonestTime.toString()
    }

    suspend fun addPetToDb (dbViewModel: PetDbViewModel, pet: Pet) {
        dbViewModel.savePet(pet = pet)
    }
}