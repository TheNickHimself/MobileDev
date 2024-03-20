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

        val resultValue = Intent().apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0)
        }
        setResult(RESULT_OK, resultValue)
        finish()
    }
}
