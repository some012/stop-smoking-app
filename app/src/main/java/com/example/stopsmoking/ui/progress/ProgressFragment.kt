package com.example.stopsmoking.ui.progress

import ProgressViewModel
import TrophiesViewModel
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
        btnSetQuitDate = view.findViewById(R.id.btn_set_quit_date)
        btnResetQuitDate = view.findViewById(R.id.btn_reset_quit_date)

        // Инициализация полей ввода
        etPricePerCigarette = view.findViewById(R.id.et_price_per_cigarette)
        etCigarettesPerDay = view.findViewById(R.id.et_cigarettes_per_day)

        // Заполнение полей данными из SharedPreferences (если есть)
        val (savedPrice, savedCigarettesPerDay) = getCigaretteData()
        etPricePerCigarette.setText(savedPrice.toString())
        etCigarettesPerDay.setText(savedCigarettesPerDay.toString())

        // Слушатели для полей ввода
        etPricePerCigarette.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                saveInputAndRecalculate()
            }
        })

        etCigarettesPerDay.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                saveInputAndRecalculate()
            }
        })

        updateProgress()

        // Установка слушателя на кнопку выбора даты
        btnSetQuitDate.setOnClickListener { showDatePickerDialog() }

        // Установка слушателя на кнопку сброса
        btnResetQuitDate.setOnClickListener { resetQuitDateToToday() }

        return view
    }

    // Метод для сохранения данных и пересчета
    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveInputAndRecalculate() {
        val rawPricePerCigarette = etPricePerCigarette.text.toString().toIntOrNull() ?: 10
        val rawCigarettesPerDay = etCigarettesPerDay.text.toString().toIntOrNull() ?: 20

        // Ограничиваем значения в заданных пределах
        val pricePerCigarette = rawPricePerCigarette.coerceIn(1, 100) // от 1 до 100 рублей
        val cigarettesPerDay = rawCigarettesPerDay.coerceIn(1, 100) // от 1 до 100 сигарет в день

        // Если введенное значение выходит за пределы, возвращаем корректное в поле ввода
        if (rawPricePerCigarette != pricePerCigarette) {
            etPricePerCigarette.setText(pricePerCigarette.toString())
        }
        if (rawCigarettesPerDay != cigarettesPerDay) {
            etCigarettesPerDay.setText(cigarettesPerDay.toString())
        }

        // Сохраняем введенные данные
        saveCigaretteData(pricePerCigarette, cigarettesPerDay)

        // Обновляем отображение
        updateProgress()
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
        val daysWithoutSmoking = java.time.temporal.ChronoUnit.DAYS.between(quitDate, LocalDate.now()).toInt()

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
