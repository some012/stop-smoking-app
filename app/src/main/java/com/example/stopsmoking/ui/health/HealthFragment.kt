package com.example.stopsmoking.ui.health

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.stopsmoking.R
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class HealthFragment : Fragment() {

    private lateinit var tvHealthStatus: TextView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_health, container, false)
        tvHealthStatus = view.findViewById(R.id.tv_health_status)

        updateHealthStatus()
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateHealthStatus() {
        val quitDate = LocalDate.of(2024, 11, 1)
        val hoursSinceQuit = ChronoUnit.HOURS.between(quitDate.atStartOfDay(), LocalDateTime.now())

        val healthMilestones = listOf(
            Pair(0, "20 минут: Давление и пульс нормализуются."),
            Pair(8, "8 часов: Уровень кислорода в крови приходит в норму."),
            Pair(24, "24 часа: Риск сердечного приступа снижен."),
            Pair(48, "48 часов: Возвращается вкус и запах."),
            Pair(72, "72 часа: Дыхание становится легче.")
        )

        val status = healthMilestones
            .filter { hoursSinceQuit >= it.first }
            .joinToString("\n") { it.second }

        tvHealthStatus.text = status.ifEmpty { "Ваше здоровье улучшается!" }
    }
}
