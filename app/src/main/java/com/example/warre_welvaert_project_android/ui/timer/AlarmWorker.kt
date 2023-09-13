package com.example.warre_welvaert_project_android.ui.timer

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.provider.AlarmClock
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.Calendar
import kotlin.random.Random

class AlarmWorker(ctx: Context, workerParams: WorkerParameters) : Worker(ctx, workerParams) {

    override fun doWork(): Result {
        val message = inputData.getString("message") ?: return Result.failure()
        val hour = inputData.getString("hour") ?: return Result.failure()
        val minute = inputData.getString("minute") ?: return Result.failure()
        setAlarm(applicationContext, message = message, hourString = hour, minuteString = minute)
        return Result.success()
    }

    private fun setAlarm(ctx: Context, message: String, hourString: String, minuteString: String) {
        val hour = hourString.toInt()
        val minute = minuteString.toInt()

        val days = listOf(
            Calendar.MONDAY,
            Calendar.TUESDAY,
            Calendar.WEDNESDAY,
            Calendar.THURSDAY,
            Calendar.FRIDAY,
            Calendar.SATURDAY,
            Calendar.SUNDAY
        )
        val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
            putExtra(AlarmClock.EXTRA_MESSAGE, message)
            putExtra(AlarmClock.EXTRA_HOUR, hour)
            putExtra(AlarmClock.EXTRA_MINUTES, minute)
            putExtra(AlarmClock.EXTRA_DAYS, days.toIntArray())
        }
        val pendingIntent = PendingIntent.getActivity(
            ctx,
            Random.nextInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        try {
            alarmManager.setWindow(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 6000,  pendingIntent)
        } catch (e: SecurityException) {
            Log.e("Alarm Error", "Failed to add alarm.")
        }
    }
}