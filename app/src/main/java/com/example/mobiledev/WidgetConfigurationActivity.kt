package com.example.mobiledev

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class WidgetConfigurationActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannel()

        setContentView(R.layout.activity_widget_configuration)

        val btnValletta = findViewById<Button>(R.id.btnValletta)
        btnValletta.setOnClickListener {
            saveSelectedCity("valletta")

        }

        val btnParis = findViewById<Button>(R.id.btnParis)
        btnParis.setOnClickListener {
            saveSelectedCity("paris")
        }

        val btnRome = findViewById<Button>(R.id.btnRome)
        btnRome.setOnClickListener {
            saveSelectedCity("rome")
        }

        val token = FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("BBBBBAAAAAAAAAAADDDDDDDDDDDDDDD", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result

            // Log and toast
            Log.d("gooooooooooooooooood", token)
        })
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = getString(R.string.default_notification_channel_id)
            val channelName = getString(R.string.default_notification_channel_name)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun saveSelectedCity(city: String) {
        val prefs = getSharedPreferences("weather_widget_prefs", MODE_PRIVATE).edit()
        prefs.putString("WeatherApp", city)
        prefs.apply()

        val intent = Intent(this, WeatherWidgetProvider::class.java).apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        }
        sendBroadcast(intent)
        finish()
    }
}
