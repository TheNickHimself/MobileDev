package com.example.mobiledev

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.widget.Button

class WidgetConfigurationActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
