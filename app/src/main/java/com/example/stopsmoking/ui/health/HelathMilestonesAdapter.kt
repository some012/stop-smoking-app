package com.example.stopsmoking.ui.health


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.stopsmoking.R

class HealthMilestonesAdapter(private val milestones: List<String>) : RecyclerView.Adapter<HealthMilestonesAdapter.MilestoneViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MilestoneViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_health_milestone, parent, false)
        return MilestoneViewHolder(view)
    }

    override fun onBindViewHolder(holder: MilestoneViewHolder, position: Int) {
        holder.milestoneDescription.text = milestones[position]
    }

    override fun getItemCount(): Int = milestones.size

    class MilestoneViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val milestoneDescription: TextView = view.findViewById(R.id.tv_milestone_description)
    }
}
