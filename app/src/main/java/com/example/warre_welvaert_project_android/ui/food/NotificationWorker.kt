package com.example.warre_welvaert_project_android.ui.food

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.warre_welvaert_project_android.R

class NotificationWorker(ctx: Context, workerParams: WorkerParameters) : Worker(ctx, workerParams) {

    override fun doWork(): Result {
        val id = inputData.getString("food_id") ?: return Result.failure()
        val name = inputData.getString("food_name") ?: return Result.failure()
        showNotification(applicationContext, name = name, id = id)
        return Result.success()
    }

    private fun showNotification(ctx: Context, name: String, id: String ) {
        val channelId = "pet_food_notify"
        val channelName = "Pet Food Notification"
        val notificationId = id.toInt()

        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, channelName, importance).apply {
            lightColor = Color.GREEN
            lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        }
        val notificationManager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        val notificationBuilder = NotificationCompat.Builder(ctx, channelId)
            .setSmallIcon(R.drawable.cat)
            .setContentTitle("Out of food")
            .setContentText("$name is almost used up.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(ctx)) {
            if (ActivityCompat.checkSelfPermission( ctx, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                Log.i("Notifications", "Failed to sent notification because permission is denied.")
            }
            notify(notificationId, notificationBuilder.build())
        }
    }
}