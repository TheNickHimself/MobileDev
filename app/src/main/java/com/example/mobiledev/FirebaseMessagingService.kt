package com.example.mobiledev


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("FireBase", "GotMessage");
        // Handle incoming FCM message
        // Extract weather alert information from the message
        val notificationData = remoteMessage.notification
        if (notificationData != null) {
            // Display a notification
            showNotification("etette", notificationData.body+"GoodPass")

            updateWidgetWithWarning()

            // Update widget without waiting for user interaction
            val appWidgetManager = AppWidgetManager.getInstance(this)
            val widgetIds = appWidgetManager.getAppWidgetIds(ComponentName(this, WeatherWidgetProvider::class.java))
            for (widgetId in widgetIds) {
                WeatherWidgetProvider.updateAppWidget(this, appWidgetManager, widgetId, showWarning = true)
            }
        }


    }


    private fun showNotification(title: String?, body: String?) {
        Log.d("FireBase", "showNotificationStart");
        val channelId = getString(R.string.default_notification_channel_id)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Weather Alerts", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        Log.d("FireBase", "MidPoint");

        val intent = Intent(this, WidgetConfigurationActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.example_appwidget_preview)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        Log.d("FireBase", "basicalyEnd");
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
