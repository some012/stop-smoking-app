package com.example.stopsmoking.ui.health

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stopsmoking.R
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class HealthFragment : Fragment() {

    private lateinit var healthRecyclerView: RecyclerView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_health, container, false)
        healthRecyclerView = view.findViewById(R.id.healthRecyclerView)

        updateHealthStatus()

        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateHealthStatus() {
        val quitDate = getQuitDate()
        val hoursSinceQuit = ChronoUnit.HOURS.between(quitDate.atStartOfDay(), LocalDateTime.now())

        val healthMilestones = listOf(
            Pair(0, "20 минут: Давление и пульс нормализуются."),
            Pair(8, "8 часов: Уровень кислорода в крови приходит в норму."),
            Pair(24, "24 часа: Риск сердечного приступа снижен."),
            Pair(48, "48 часов: Возвращается вкус и запах."),
            Pair(72, "72 часа: Дыхание становится легче."),
            Pair(1 * 7 * 24, "1 неделя: Уровень угарного газа в организме нормализуется."),
            Pair(2 * 7 * 24, "2 недели: Улучшается циркуляция крови и функция легких."),
            Pair(1 * 30 * 24, "1 месяц: Снижается риск развития заболеваний сердца."),
            Pair(3 * 30 * 24, "3 месяца: Дыхательная функция значительно улучшается."),
            Pair(6 * 30 * 24, "6 месяцев: Улучшается состояние кожи и волос."),
            Pair(1 * 365 * 24, "1 год: Риск сердечных заболеваний уменьшается на 50%.")
        )

        val milestones = healthMilestones
            .filter { hoursSinceQuit >= it.first }
            .map { it.second }

        val adapter = HealthMilestonesAdapter(milestones)
        healthRecyclerView.layoutManager = LinearLayoutManager(context)
        healthRecyclerView.adapter = adapter
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
}
