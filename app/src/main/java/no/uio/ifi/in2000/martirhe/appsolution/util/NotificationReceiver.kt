package no.uio.ifi.in2000.martirhe.appsolution.util

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import no.uio.ifi.in2000.martirhe.appsolution.ui.screens.notification.CreateNotification
import java.util.Calendar

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH) + 1
        if (currentMonth in 4..10) { // Only April to October
            val notificationText = "Plask savner deg, kom å sjekk ut dine favoritt badesteder"
            CreateNotification(context, "channel_01", "Daglig påminnelse", notificationText)
        }
    }
}

@SuppressLint("ServiceCast")
fun setRepeatingAlarm(context: Context) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, NotificationReceiver::class.java).apply {
        action = "no.uio.ifi.in2000.martirhe.appsolution.util"
    }
    val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

    val threeDaysMillis = 259_200_000L // 3 days in milliseconds
    val sixtyFourHoursMillis = 230_400_000L // 64 hours in milliseconds
    val triggerTime = System.currentTimeMillis() + threeDaysMillis

    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, triggerTime, sixtyFourHoursMillis, pendingIntent)
}