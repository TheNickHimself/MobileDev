package com.example.mobiledev

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
<<<<<<< Updated upstream
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class WidgetConfigurationActivity : Activity() {

=======
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class WidgetConfigurationActivity : AppCompatActivity() {
    private val BatteryReceiver = BatteryReceiver()
>>>>>>> Stashed changes
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannel()

        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
        }
        registerReceiver(BatteryReceiver, filter)

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
<<<<<<< Updated upstream
=======



    internal fun fetchDataAndUpdateWidget(cityName: String) {

        val BASE_URL = "https://api.jamesdecelis.com/"

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(WeatherApiService::class.java)


        //val apiService: WeatherApiService = Retrofit.create(WeatherApiService::class.java)
        lifecycleScope.launch(Dispatchers.IO) {
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
>>>>>>> Stashed changes

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
