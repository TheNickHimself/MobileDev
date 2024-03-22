package com.example.mobiledev

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.widget.RemoteViews
import android.content.Intent
import retrofit2.Call
import android.util.Log


class WeatherWidgetProvider : AppWidgetProvider() {

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == AppWidgetManager.ACTION_APPWIDGET_UPDATE) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(context, WeatherWidgetProvider::class.java))
            onUpdate(context, appWidgetManager, appWidgetIds)
        }
    }
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {


        Log.d("onUpdate", "triggered") // Debug level log
        for (appWidgetId in appWidgetIds) {
            val intent = Intent(context, WidgetConfigurationActivity::class.java)
            val weatherName = intent.getStringExtra("weatherName")
            val weatherTemp = intent.getIntExtra("weatherTemp", -300) // Default value if not found
            val weatherCondition = intent.getStringExtra("weatherCondition")

            updateAppWidget(context, appWidgetManager, appWidgetId, false, weatherName, weatherTemp, weatherCondition)        }
    }



    companion object {
        internal fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int,
            showWarning: Boolean = false,

            weatherName: String?,
            weatherTemp: Int?,
            weatherCondition: String?
        ) {
            Log.d("updateAppWidget", "triggered")

                    val views = RemoteViews(context.packageName, R.layout.widget_layout)

                        views.setTextViewText(R.id.txtLocation, weatherName)
                        views.setTextViewText(R.id.txtTemperature, "$weatherTemp °C")
                        views.setTextViewText(R.id.txtWeatherStatus, weatherCondition)


                    Log.d("weatherResponse", "finished and sent to views")
                    val bgColor = if (showWarning) {
                        android.R.color.holo_red_light // Change this to the desired color resource
                    } else {
                        android.R.color.transparent // Change this to the default background color
                    }
                    views.setInt(R.id.widget_layout, "setBackgroundResource", bgColor)
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
            }
        }



