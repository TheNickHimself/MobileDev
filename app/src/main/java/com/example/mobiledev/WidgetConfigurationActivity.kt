package com.example.mobiledev

import android.app.NotificationChannel
import android.app.NotificationManager
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class WidgetConfigurationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannel()

        setContentView(R.layout.activity_widget_configuration)


        val btnValletta = findViewById<Button>(R.id.btnValletta)
        btnValletta.setOnClickListener {
            fetchDataAndUpdateWidget("valletta")

        }

        val btnParis = findViewById<Button>(R.id.btnParis)
        btnParis.setOnClickListener {
            fetchDataAndUpdateWidget("paris")
        }

        val btnRome = findViewById<Button>(R.id.btnRome)
        btnRome.setOnClickListener {
            fetchDataAndUpdateWidget("rome")
        }

        val token = FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result

            // Log and toast
            Log.d("Token", token)
        })
    }
    internal fun fetchDataAndUpdateWidget(cityName: String) {

        val BASE_URL = "https://api.jamesdecelis.com/"

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(WeatherApiService::class.java)


        //val apiService: WeatherApiService = Retrofit.create(WeatherApiService::class.java)
        lifecycleScope.launch(Dispatchers.IO,) {
                val reply = apiService.getWeather(cityName)
                if (reply.isSuccessful) {
                    val weatherResponse = reply.body()
                    if (weatherResponse != null) {
                        saveSelectedCity(weatherResponse)
                    } else {
                        Log.e("WidgetConfiguration", "Weather response body is null")
                    }
                }
        }

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

    private fun saveSelectedCity(weatherResponse: WeatherResponse) {
        val appWidgetManager = AppWidgetManager.getInstance(this)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(this, WeatherWidgetProvider::class.java))
        for (appWidgetId in appWidgetIds) {
            WeatherWidgetProvider.updateAppWidget(this, appWidgetManager, appWidgetId, false, weatherResponse.name, weatherResponse.temp, weatherResponse.condition)
        }
        finish()
    }


}
