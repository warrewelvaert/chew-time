package com.example.warre_welvaert_project_android.ui.timer

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.warre_welvaert_project_android.model.Pet
import java.time.Duration
import java.time.LocalTime

class TimerViewModel: ViewModel() {

    private fun setAlarm(ctx: Context, pet: Pet, index: Int) {
        val inputData = Data.Builder()
            .putString("message", "Time to feed ${pet.name}!")
            .putString("hour", pet.feedingTimes[index].hour.toString())
            .putString("minute", pet.feedingTimes[index].minute.toString())
            .build()

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .build()

        val alarmWorker = OneTimeWorkRequestBuilder<AlarmWorker>()
            .setInputData(inputData)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(ctx).enqueueUniqueWork(
            "alarm_work_${pet.name}_${pet.feedingTimes[index]}",
            ExistingWorkPolicy.REPLACE,
            alarmWorker
        )
    }

    fun createAlarmsForPet(ctx: Context, pet: Pet) {
        pet.feedingTimes.forEachIndexed { index, _ ->
            setAlarm( ctx = ctx, pet = pet, index = index )
        }
    }

    fun calculateTimeDifference(time: LocalTime?): Duration {
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

    fun getNextTime(pet: Pet): LocalTime? {
        if (pet.feedingTimes.isEmpty()) {
            return null
        }
        return pet.feedingTimes.minByOrNull { calculateTimeDifference(it) }
    }
}