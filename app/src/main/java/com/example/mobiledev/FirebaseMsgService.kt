package com.example.mobiledev

// MyFirebaseMessagingService.kt
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMsgService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Handle incoming FCM message
        // Extract weather alert information from the message
        val notificationData = remoteMessage.notification
        if (notificationData != null) {
            // Display a notification
            showNotification(notificationData.title, notificationData.body)

            // Update widget with red warning (message received)
            updateWidgetWithWarning()
        }
    }

    private fun showNotification(title: String?, body: String?) {
        val channelId = "WeatherAlertsChannel"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Weather Alerts", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, WidgetConfigurationActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(body)
            //.setSmallIcon(R.drawable.notification_icon)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun updateWidgetWithWarning() {
        val appWidgetManager = AppWidgetManager.getInstance(this)
        val widgetIds = appWidgetManager.getAppWidgetIds(ComponentName(this, WeatherWidgetProvider::class.java))

        for (widgetId in widgetIds) {
            WeatherWidgetProvider.updateAppWidget(this, appWidgetManager, widgetId, showWarning = true)
        }
    }
}
