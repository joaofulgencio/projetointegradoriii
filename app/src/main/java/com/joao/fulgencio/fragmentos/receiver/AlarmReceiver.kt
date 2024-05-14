package com.joao.fulgencio.fragmentos.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.joao.fulgencio.fragmentos.R

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val message = intent?.getStringExtra("message") ?: "Teste"
        showNotification(context, message)
    }

    private fun showNotification(context: Context, message: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel("default", "Default Channel", NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)

        val builder = NotificationCompat.Builder(context, "default")
            .setContentTitle("Lembrete")
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_foreground)

        notificationManager.notify(1, builder.build())
    }
}