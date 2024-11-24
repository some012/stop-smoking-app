package com.example.stopsmoking.ui.rewards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stopsmoking.R

class RewardsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RewardsAdapter
    private val rewards = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rewards, container, false)
        recyclerView = view.findViewById(R.id.rewardsRecyclerView)
        val rewardInput: EditText = view.findViewById(R.id.rewardInput)
        val addRewardButton: Button = view.findViewById(R.id.addRewardButton)

        adapter = RewardsAdapter(rewards)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        addRewardButton.setOnClickListener {
            val reward = rewardInput.text.toString()
            if (reward.isNotBlank()) {
                rewards.add(reward)
                adapter.notifyItemInserted(rewards.size - 1)
                rewardInput.text.clear()
            }
        }

        return view
    }
}
