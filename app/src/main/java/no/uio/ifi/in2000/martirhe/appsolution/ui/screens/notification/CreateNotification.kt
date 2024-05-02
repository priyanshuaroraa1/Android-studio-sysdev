package no.uio.ifi.in2000.martirhe.appsolution.ui.screens.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import no.uio.ifi.in2000.martirhe.appsolution.R

@SuppressLint("MissingPermission")
fun CreateNotification(context: Context, channelId: String, title: String, text: String) {
    val notificationManager = NotificationManagerCompat.from(context)

    val channel = NotificationChannel(channelId, "Varslinger", NotificationManager.IMPORTANCE_DEFAULT).apply {
        description = "Varlsingskanal for Plask"
        lightColor = Color.BLUE
        enableLights(true)
    }
    notificationManager.createNotificationChannel(channel)

    val notification = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.pin)
        .setContentTitle(title)
        .setContentText(text)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)
        .build()

    notificationManager.notify(1, notification)
}