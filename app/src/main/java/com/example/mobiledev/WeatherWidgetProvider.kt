package com.example.mobiledev

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.view.View
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log
import android.widget.Button


class WeatherWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {

        Log.d("onUpdate", "triggered") // Debug level log
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }


    companion object {
        internal fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            Log.d("updateAppWidget", "triggered")
            val prefs = context.getSharedPreferences("weather_widget_prefs", MODE_PRIVATE)
            val selectedCity = prefs.getString("WeatherApp", "") ?: ""

            val service = RetrofitInstance.retrofit.create(WeatherApiService::class.java)
            val call = service.getWeather(selectedCity)
            call.enqueue(object : Callback<WeatherResponse> {
                override fun onResponse(
                    call: Call<WeatherResponse>,
                    response: Response<WeatherResponse>
                ) {
                    Log.d("onResponse", "triggered")
                    val weatherResponse = response.body()
                    Log.d("weatherResponse", "started")
                    val views = RemoteViews(context.packageName, R.layout.widget_layout)
                    if (weatherResponse != null) {
                        views.setTextViewText(R.id.txtLocation, weatherResponse.name)
                        views.setTextViewText(R.id.txtTemperature, "${weatherResponse.temp} °C")
                        views.setTextViewText(R.id.txtWeatherStatus, weatherResponse.condition)
                    }

                    Log.d("weatherResponse", "finished and sent to views")
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }

                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    Log.d("weatherResponse", "FAILED")
                }
            })
        }
    }

}
