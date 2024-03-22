package com.example.mobiledev

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ChargeHistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChargeHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_charge_history)

        recyclerView = findViewById(R.id.charge_history_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ChargeHistoryAdapter(loadChargeHistory())
        recyclerView.adapter = adapter
    }

    private fun loadChargeHistory(): List<Long> {
        val sharedPreferences: SharedPreferences = getSharedPreferences("ChargerPrefs", Context.MODE_PRIVATE)
        val chargeHistorySet = sharedPreferences.getStringSet("ChargeHistory", HashSet()) ?: HashSet()
        return chargeHistorySet.map { it.toLong() }
    }
}
