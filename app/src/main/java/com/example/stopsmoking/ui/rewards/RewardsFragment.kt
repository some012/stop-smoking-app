package com.example.stopsmoking.ui.rewards

import ProgressViewModel
import RewardsViewModel
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stopsmoking.R
import androidx.fragment.app.activityViewModels

class RewardsFragment : Fragment() {

    private val rewardsViewModel: RewardsViewModel by activityViewModels()
    private val progressViewModel: ProgressViewModel by activityViewModels()


    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RewardsAdapter
    private lateinit var tvBalance: TextView
    private var balance: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rewards, container, false)
        recyclerView = view.findViewById(R.id.rewardsRecyclerView)
        val rewardInput: EditText = view.findViewById(R.id.rewardInput)
        val rewardCostInput: EditText = view.findViewById(R.id.rewardCostInput)
        val addRewardButton: Button = view.findViewById(R.id.addRewardButton)
        val resetButton: Button = view.findViewById(R.id.resetButton)
        tvBalance = view.findViewById(R.id.tv_balance)

        // Загружаем данные из ViewModel
        rewardsViewModel.loadData(requireContext())
        balance = rewardsViewModel.getRewardsBalance()
        tvBalance.text = "Баланс: $balance руб."

        // Инициализация адаптера и передача наград
        adapter = RewardsAdapter(rewardsViewModel.getRewards().toMutableList())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        addRewardButton.setOnClickListener {
            val reward = rewardInput.text.toString()
            val cost = rewardCostInput.text.toString().toIntOrNull()

            if (reward.isNotBlank() && cost != null) {
                if (balance >= cost) {
                    // Добавление награды
                    rewardsViewModel.addReward("$reward - $cost руб.")
                    balance -= cost
                    tvBalance.text = "Баланс: $balance руб."
                    rewardsViewModel.setProgressBalance(balance)
                    rewardsViewModel.saveData(requireContext()) // Сохраняем данные после изменения
                    adapter.addReward("$reward - $cost руб.") // Обновляем адаптер
                    rewardInput.text.clear()
                    rewardCostInput.text.clear()
                } else {
                    rewardInput.error = "Недостаточно средств!"
                }
            }
        }

        resetButton.setOnClickListener {
            rewardsViewModel.clearRewards() // Очищаем награды в ViewModel
            balance = progressViewModel.getProgressBalance() // Получаем баланс из прогресса
            rewardsViewModel.setProgressBalance(balance) // Устанавливаем новый баланс в награды
            tvBalance.text = "Баланс: $balance руб." // Обновляем отображение баланса
            adapter.updateRewards(emptyList()) // Очищаем адаптер
            rewardsViewModel.saveData(requireContext()) // Сохраняем изменения
        }

        return view
    }


}
