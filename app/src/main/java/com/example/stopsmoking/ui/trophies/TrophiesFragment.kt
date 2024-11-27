import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.stopsmoking.R

class TrophiesFragment : Fragment() {

    private val trophiesViewModel: TrophiesViewModel by activityViewModels()
    private lateinit var listView: ListView
    private lateinit var tvTip: TextView
    private lateinit var adapter: TrophiesListAdapter

    // Карта достижений и изображений
    private val trophyImagesMap = mapOf(
        "1 день без курения" to R.drawable.check,
        "1 неделя без курения" to R.drawable.check,
        "1 месяц без курения" to R.drawable.check,
        "3 месяца без курения" to R.drawable.check,
        "6 месяцев без курения" to R.drawable.check,
        "1 год без курения" to R.drawable.check,
        "1000 дней без курения" to R.drawable.check,
        "5000 дней без курения" to R.drawable.check,
        "Сэкономлено 1000 рублей" to R.drawable.check,
        "Сэкономлено 5000 рублей" to R.drawable.check,
        "Сэкономлено 10000 рублей" to R.drawable.check
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_trophies, container, false)
        listView = view.findViewById(R.id.trophiesListView)
        tvTip = view.findViewById(R.id.tv_tip)

        // Инициализация адаптера
        adapter = TrophiesListAdapter(requireContext(), mutableListOf(), trophyImagesMap)
        listView.adapter = adapter

        // Подписка на изменения ViewModel
        trophiesViewModel.trophies.observe(viewLifecycleOwner, Observer { trophies ->
            adapter.updateTrophies(trophies)
        })

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

        return view
    }
}
