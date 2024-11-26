package com.example.stopsmoking.ui.progress

import ProgressViewModel
import TrophiesViewModel
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.stopsmoking.R
import java.time.LocalDate
import java.time.Period

class ProgressFragment : Fragment() {

    private lateinit var tvDaysWithoutSmoking: TextView
    private lateinit var tvCigarettesAvoided: TextView
    private lateinit var tvMoneySaved: TextView
    private lateinit var tvLifeSaved: TextView
    private lateinit var tvTip: TextView
    private val progressViewModel: ProgressViewModel by activityViewModels()
    private val trophiesViewModel: TrophiesViewModel by activityViewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_progress, container, false)

        tvDaysWithoutSmoking = view.findViewById(R.id.tv_days_without_smoking)
        tvCigarettesAvoided = view.findViewById(R.id.tv_cigarettes_avoided)
        tvMoneySaved = view.findViewById(R.id.tv_money_saved)
        tvLifeSaved = view.findViewById(R.id.tv_life_saved)
        tvTip = view.findViewById(R.id.tv_tip)

        updateProgress()
        return view
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateProgress() {
        val quitDate = LocalDate.of(2024, 11, 1) // Пример: дата отказа от курения
        val daysWithoutSmoking = Period.between(quitDate, LocalDate.now()).days
        val cigarettesAvoided = daysWithoutSmoking * 20
        val moneySaved = cigarettesAvoided * 10 // Цена одной сигареты, допустим, 10 рублей
        val lifeSavedMinutes = cigarettesAvoided * 11 // Пример: 11 минут жизни на сигарету

        // Устанавливаем данные
        tvDaysWithoutSmoking.text = "Дней без курения: $daysWithoutSmoking"
        tvCigarettesAvoided.text = "Не выкурено сигарет: $cigarettesAvoided"
        tvMoneySaved.text = "Сэкономлено денег: $moneySaved руб."
        tvLifeSaved.text = "Сохранено времени жизни: $lifeSavedMinutes минут"

        // Обновляем баланс
        progressViewModel.setProgressBalance(moneySaved)

        // Мотивационный совет
        val tips = listOf(
            "Вы становитесь лучше каждый день!",
            "Подумайте, сколько вы уже сэкономили!",
            "Ваше здоровье вас благодарит!",
            "Каждый день без сигарет – это шаг к лучшей жизни.",
            "Ваша сила воли впечатляет!",
            "Подумайте о свободе, которую вы получили!",
            "Ваши легкие дышат полной грудью.",
            "Каждый вдох – это победа над зависимостью.",
            "Ваши близкие гордятся вами!",
            "Вы уже сэкономили деньги на что-то полезное!",
            "Сегодня вы сделали важный шаг к здоровью.",
            "С каждым днем ваше тело восстанавливается.",
            "Ваш пример вдохновляет других.",
            "Вы доказываете, что можете всё!",
            "Помните, как много вы уже достигли!",
            "Заботьтесь о себе – вы это заслужили!",
            "Ваше сердце стало здоровее благодаря вам.",
            "Ваша решимость – ваша суперсила.",
            "Каждый день – это победа над прошлым.",
            "Продолжайте идти вперед, вы на правильном пути!"
        )

        tvTip.text = tips.random()

        // Обновление достижений
        trophiesViewModel.updateTrophies(daysWithoutSmoking, moneySaved)

        // Обновляем баланс в ProgressViewModel
        progressViewModel.setProgressBalance(moneySaved)
    }
}
