package com.example.stopsmoking.ui.rewards

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RewardsViewModel : ViewModel() {

    private val _rewards = MutableLiveData<MutableList<String>>(mutableListOf())
    val rewards: LiveData<MutableList<String>> get() = _rewards

    private var rewardsBalance: Int = 0

    fun getRewardsBalance(): Int = rewardsBalance

    fun getRewards(): List<String> = _rewards.value ?: emptyList()

    fun addReward(reward: String) {
        _rewards.value?.add(reward)
        _rewards.postValue(_rewards.value)
    }

    fun clearRewards() {
        _rewards.value?.clear()
        _rewards.postValue(_rewards.value)
    }

    // Новый метод для обновления баланса
    fun setProgressBalance(balance: Int) {
        rewardsBalance = balance
        Log.d("RewardsViewModel", "Обновлён баланс: $rewardsBalance") // Отладочный вывод
    }

    fun saveData(context: Context) {
        val sharedPreferences = context.getSharedPreferences("RewardsData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Сохраняем баланс
        editor.putInt("balance", rewardsBalance)
        Log.d("RewardsViewModel", "Сохранён баланс: $rewardsBalance") // Добавим отладочный вывод

        // Сохраняем список наград
        val rewardsSet = _rewards.value?.toSet() ?: emptySet() // Преобразуем список в Set
        editor.putStringSet("rewards", rewardsSet)

        editor.apply()
    }

    fun loadData(context: Context) {
        val sharedPreferences = context.getSharedPreferences("RewardsData", Context.MODE_PRIVATE)

        // Загружаем баланс
        rewardsBalance = sharedPreferences.getInt("balance", 0)
        Log.d("RewardsViewModel", "Загружен баланс: $rewardsBalance") // Отладочный вывод

        // Загружаем список наград
        val rewardsSet = sharedPreferences.getStringSet("rewards", emptySet()) ?: emptySet()
        _rewards.value = rewardsSet.toMutableList()
    }
}
