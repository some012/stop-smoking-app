import android.annotation.SuppressLint
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
import java.time.Period

class ProgressFragment : Fragment() {

    private lateinit var tvDaysWithoutSmoking: TextView
    private lateinit var tvCigarettesAvoided: TextView
    private lateinit var tvMoneySaved: TextView
    private lateinit var tvLifeSaved: TextView
    private lateinit var tvTip: TextView

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
        // Примерные данные. Замените на данные от пользователя.
        val quitDate = LocalDate.of(2024, 11, 1) // Дата отказа от курения
        val daysWithoutSmoking = Period.between(quitDate, LocalDate.now()).days
        val cigarettesAvoided = daysWithoutSmoking * 20
        val moneySaved = cigarettesAvoided * 10 // Цена одной сигареты, допустим, 10 рублей
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
            "Ваше здоровье вас благодарит!"
        )
        tvTip.text = "Совет: ${tips.random()}"
    }
}