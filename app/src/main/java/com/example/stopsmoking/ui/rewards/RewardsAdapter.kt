package com.example.stopsmoking.ui.rewards

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stopsmoking.R

class RewardsAdapter(private val rewards: MutableList<String>) :
    RecyclerView.Adapter<RewardsAdapter.RewardsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RewardsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_reward, parent, false)
        return RewardsViewHolder(view)
    }

    override fun onBindViewHolder(holder: RewardsViewHolder, position: Int) {
        holder.bind(rewards[position])
    }

    override fun getItemCount(): Int = rewards.size

    // Добавление новой награды
    fun addReward(reward: String) {
        rewards.add(reward)
        notifyItemInserted(rewards.size - 1) // Уведомляем об изменении
    }

    // Обновление списка наград
    @SuppressLint("NotifyDataSetChanged")
    fun updateRewards(newRewards: List<String>) {
        rewards.clear() // Очищаем текущий список
        rewards.addAll(newRewards) // Добавляем новый список
        notifyDataSetChanged() // Уведомляем об изменении всего списка
    }

    inner class RewardsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val rewardTextView: TextView = itemView.findViewById(R.id.rewardInput)

        fun bind(reward: String) {
            rewardTextView.text = reward
        }
    }
}
