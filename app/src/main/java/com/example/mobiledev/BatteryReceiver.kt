package com.example.mobiledev;

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.util.Log
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class BatteryReceiver : BroadcastReceiver() {
// genuinely no clue how this works i couldn't even get this to trigger onece. I spent ages scrolling through Stackoverflow Androdi docs etc
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("BatteryReceiver", "onReceive")

    intent?.let {
        when (it.action) {
            Intent.ACTION_POWER_CONNECTED -> {
                // Handle power connected event
                Log.d("PowerConnectionReceiver", "Power Connected")
                if(context != null)
                    powerConnect(context, intent, true) else TODO()
            }
            Intent.ACTION_POWER_DISCONNECTED -> {
                // Handle power disconnected event
                Log.d("PowerConnectionReceiver", "Power disconnected")
                if(context != null)
                    powerConnect(context, intent, false) else TODO()
            }
            Intent.ACTION_BATTERY_LOW -> {
                Log.d("PowerConnectionReceiver", "Battery Low")
                val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                val batteryPct = level / scale.toFloat() * 100


                val updateIntent = Intent(context, BatteryWidgetProvider::class.java)
                updateIntent.putExtra("batteryLevel", batteryPct)
                context?.sendBroadcast(updateIntent)
            }
            else ->{
                Log.d("BatteryReceiver", "something not good happened")
            }
        }
    }
    }
    private fun powerConnect(context: Context, intent: Intent, connected: Boolean){
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        var current = LocalDateTime.now().format(formatter)
        val updateIntent = Intent(context, BatteryWidgetProvider::class.java)

        if (connected){
            updateIntent.putExtra("isCharging", -1)
            current += " Connected"
        }else{
            updateIntent.putExtra("isCharging", 0)
            current += " Disconnected"
        }

        val sharedPreferences = context.getSharedPreferences("chargeHistory", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("charge", current)
        editor.apply()


        context?.sendBroadcast(updateIntent)
    }
}