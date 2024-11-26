package com.example.stopsmoking.ui.progress

import ProgressViewModel
import TrophiesViewModel
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.stopsmoking.R
import java.time.LocalDate
import java.time.Period
import java.util.*

class ProgressFragment : Fragment() {

    private lateinit var tvDaysWithoutSmoking: TextView
    private lateinit var tvCigarettesAvoided: TextView
    private lateinit var tvMoneySaved: TextView
    private lateinit var tvLifeSaved: TextView
    private lateinit var tvTip: TextView
    private lateinit var btnSetQuitDate: Button
    private lateinit var btnResetQuitDate: Button
    private lateinit var etPricePerCigarette: EditText
    private lateinit var etCigarettesPerDay: EditText

    private val progressViewModel: ProgressViewModel by activityViewModels()
    private val trophiesViewModel: TrophiesViewModel by activityViewModels()

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_progress, container, false)

        // Инициализация элементов UI
        tvDaysWithoutSmoking = view.findViewById(R.id.tv_days_without_smoking)
        tvCigarettesAvoided = view.findViewById(R.id.tv_cigarettes_avoided)
        tvMoneySaved = view.findViewById(R.id.tv_money_saved)
        tvLifeSaved = view.findViewById(R.id.tv_life_saved)
        tvTip = view.findViewById(R.id.tv_tip)
        btnSetQuitDate = view.findViewById(R.id.btn_set_quit_date)
        btnResetQuitDate = view.findViewById(R.id.btn_reset_quit_date)

        // Инициализация полей ввода
        val etPricePerCigarette: EditText = view.findViewById(R.id.et_price_per_cigarette)
        val etCigarettesPerDay: EditText = view.findViewById(R.id.et_cigarettes_per_day)

        // Заполнение полей данными из SharedPreferences (если есть)
        val (savedPrice, savedCigarettesPerDay) = getCigaretteData()
        etPricePerCigarette.setText(savedPrice.toString())
        etCigarettesPerDay.setText(savedCigarettesPerDay.toString())

        updateProgress()

        // Установка слушателя на кнопку выбора даты
        btnSetQuitDate.setOnClickListener { showDatePickerDialog() }

        // Установка слушателя на кнопку сброса
        btnResetQuitDate.setOnClickListener { resetQuitDateToToday() }

        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDatePickerDialog() {
        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay)
            saveQuitDate(selectedDate)

            // Сохранение данных о цене и количестве сигарет
            val pricePerCigarette = etPricePerCigarette.text.toString().toIntOrNull() ?: 10
            val cigarettesPerDay = etCigarettesPerDay.text.toString().toIntOrNull() ?: 20
            saveCigaretteData(pricePerCigarette, cigarettesPerDay)

            updateProgress()
        }, year, month, day)

        // Ограничиваем минимальную дату (например, не разрешаем выбрать будущие даты)
        datePickerDialog.datePicker.maxDate = currentDate.timeInMillis // Максимальная дата — сегодняшняя

        datePickerDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun resetQuitDateToToday() {
        val today = LocalDate.now()
        saveQuitDate(today)

        saveCigaretteData(10, 20) // Значения по умолчанию

        updateProgress()
    }

    // Функция для сохранения данных о цене сигареты и количестве сигарет в день
    private fun saveCigaretteData(price: Int, cigarettesPerDay: Int) {
        val sharedPreferences = requireContext().getSharedPreferences("StopSmokingPrefs", 0)
        val editor = sharedPreferences.edit()
        editor.putInt("pricePerCigarette", price)
        editor.putInt("cigarettesPerDay", cigarettesPerDay)
        editor.apply()
    }

    // Получение данных о цене сигареты и количестве сигарет в день
    private fun getCigaretteData(): Pair<Int, Int> {
        val sharedPreferences = requireContext().getSharedPreferences("StopSmokingPrefs", 0)
        val pricePerCigarette = sharedPreferences.getInt("pricePerCigarette", 10) // Значение по умолчанию
        val cigarettesPerDay = sharedPreferences.getInt("cigarettesPerDay", 20) // Значение по умолчанию
        return Pair(pricePerCigarette, cigarettesPerDay)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateProgress() {
        val quitDate = getQuitDate()
        val daysWithoutSmoking = Period.between(quitDate, LocalDate.now()).days

        // Получаем данные о цене сигареты и количестве сигарет в день
        val (pricePerCigarette, cigarettesPerDay) = getCigaretteData()

        // Расчеты на основе новых данных
        val cigarettesAvoided = daysWithoutSmoking * cigarettesPerDay
        val moneySaved = cigarettesAvoided * pricePerCigarette // С учетом новой цены

        val lifeSavedMinutes = cigarettesAvoided * 11 // Пример: 11 минут жизни на сигарету

        // Устанавливаем данные
        tvDaysWithoutSmoking.text = "Дней без курения: $daysWithoutSmoking"
        tvCigarettesAvoided.text = "Не выкурено сигарет: $cigarettesAvoided"
        tvMoneySaved.text = "Сэкономлено денег: $moneySaved руб."
        tvLifeSaved.text = "Сохранено времени жизни: $lifeSavedMinutes минут"

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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getQuitDate(): LocalDate {
        val sharedPreferences = requireContext().getSharedPreferences("StopSmokingPrefs", 0)
        val quitDateString = sharedPreferences.getString("quitDate", null)
        return if (quitDateString != null) {
            LocalDate.parse(quitDateString)
        } else {
            LocalDate.now()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveQuitDate(date: LocalDate) {
        val sharedPreferences = requireContext().getSharedPreferences("StopSmokingPrefs", 0)
        val editor = sharedPreferences.edit()
        editor.putString("quitDate", date.toString())
        editor.apply()
    }
}
