package com.example.mobiledev

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class ChargeHistoryAdapter(loadChargeHistory: List<Long>) : ListAdapter<Long, ChargeHistoryAdapter.ChargeHistoryViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChargeHistoryViewHolder {
        return ChargeHistoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.charge_history_item, parent, false))
    }

    override fun onBindViewHolder(holder: ChargeHistoryViewHolder, position: Int) {
        val timestamp = getItem(position)
        holder.bind(timestamp)
    }

    inner class ChargeHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val chargeTimeTextView: TextView = itemView.findViewById(R.id.item_chargeTxt)

        fun bind(timestamp: Long) {
            chargeTimeTextView.text = timestamp.toString()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Long>() {
        override fun areItemsTheSame(oldItem: Long, newItem: Long): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Long, newItem: Long): Boolean {
            return oldItem == newItem
        }
    }
}
