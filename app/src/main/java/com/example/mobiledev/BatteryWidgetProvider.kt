package com.example.mobiledev

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Context.BATTERY_SERVICE
import android.content.Intent
import android.os.BatteryManager
import android.util.Log
import android.widget.RemoteViews


/**
 * Implementation of App Widget functionality.
 */
class BatteryWidgetProvider : AppWidgetProvider() {

    companion object {
        private const val DOUBLE_CLICK_DELAY = 300 // Adjust delay as needed
        private var lastClickTime: Long = 0
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(context, BatteryWidgetProvider::class.java))
            val isCharging = intent.getIntExtra("isCharging", -2)
        if (context != null) {
            for (appWidgetId in appWidgetIds) {

                updateBattery(context, AppWidgetManager.getInstance(context), appWidgetId, isCharging)
            }
        }
    }
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val bm = context.getSystemService(BATTERY_SERVICE) as BatteryManager
        val batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)

        val intent = Intent(context, ChargeHistoryActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE)

        for (appWidgetId in appWidgetIds) {


            val views = RemoteViews(context.packageName, R.layout.battery_widget)

            views.setOnClickPendingIntent(R.id.widget_container, pendingIntent)

            updateBattery(context, appWidgetManager, appWidgetId, batLevel)
        }
    }
}

internal fun updateBattery(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int,
    isCharging: Int = -2,
) {
    val bm = context.getSystemService(BATTERY_SERVICE) as BatteryManager
    val batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
    val views = RemoteViews(context.packageName, R.layout.battery_widget)
    if (isCharging == -1){
        views.setImageViewResource(R.id.batteryImg, R.drawable.charging);
    }else if (batLevel > 0 && batLevel <= 100){
        if (batLevel >= 75) {
            views.setImageViewResource(R.id.batteryImg, R.drawable.green_full);
        } else if (batLevel >= 45) {
            views.setImageViewResource(R.id.batteryImg, R.drawable.orange_half);
        } else {
            views.setImageViewResource(R.id.batteryImg, R.drawable.red_low);
        }
    }else{
        Log.d("updateBattery", "something not good happened")
    }
    appWidgetManager.updateAppWidget(appWidgetId, views)

}
