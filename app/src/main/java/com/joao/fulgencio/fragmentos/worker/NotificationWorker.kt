package com.joao.fulgencio.fragmentos.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class NotificationWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    companion object {
        const val CHANNEL_ID = "scheduled_notifications"
        const val NOTIFICATION_TITLE = "notification_title"
        const val NOTIFICATION_MESSAGE = "notification_message"
    }

    override fun doWork(): Result {
        val title = inputData.getString(NOTIFICATION_TITLE) ?: "Scheduled Notification"
        val message = inputData.getString(NOTIFICATION_MESSAGE) ?: "Your scheduled notification"

        showNotification(title, message)
        return Result.success()
    }

    private fun showNotification(title: String, message: String) {
        // Cria um canal de notificação para Android 8.0+
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Scheduled Notifications",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        // Configura a notificação
        val notificationBuilder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        notificationManager.notify(1, notificationBuilder.build())
    }
}