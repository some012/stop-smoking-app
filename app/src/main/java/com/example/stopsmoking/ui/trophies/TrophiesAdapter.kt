package com.example.stopsmoking.ui.trophies

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stopsmoking.R

class TrophiesAdapter(private var trophies: List<String>) :
    RecyclerView.Adapter<TrophiesAdapter.TrophyViewHolder>() {

    class TrophyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val trophyTextView: TextView = itemView.findViewById(R.id.trophyTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrophyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_trophy, parent, false)
        return TrophyViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrophyViewHolder, position: Int) {
        holder.trophyTextView.text = trophies[position]
    }

    override fun getItemCount(): Int = trophies.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateTrophies(newTrophies: List<String>) {
        trophies = newTrophies
        notifyDataSetChanged()
    }
}

