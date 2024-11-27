import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TrophiesViewModel : ViewModel() {

    private val _trophies = MutableLiveData<List<String>>()
    val trophies: LiveData<List<String>> get() = _trophies

    private val achievementThresholds = listOf(
        Pair(1, "1 день без курения"),
        Pair(7, "1 неделя без курения"),
        Pair(30, "1 месяц без курения"),
        Pair(90, "3 месяца без курения"),
        Pair(180, "6 месяцев без курения"),
        Pair(365, "1 год без курения"),
    )

    fun updateTrophies(daysWithoutSmoking: Int, moneySaved: Int) {
        val achievements = mutableListOf<String>()

        // Добавляем достижения на основе количества дней
        achievements.addAll(
            achievementThresholds.filter { daysWithoutSmoking >= it.first }
                .map { it.second }
        )

        // Добавляем достижения на основе сэкономленных денег
        if (moneySaved >= 500) achievements.add("Сэкономлено 500 рублей")
        if (moneySaved >= 1000) achievements.add("Сэкономлено 1000 рублей")
        if (moneySaved >= 5000) achievements.add("Сэкономлено 5000 рублей")
        if (moneySaved >= 10000) achievements.add("Сэкономлено 10000 рублей")
        if (moneySaved >= 20000) achievements.add("Сэкономлено 20000 рублей")
        if (moneySaved >= 50000) achievements.add("Сэкономлено 50000 рублей")

        // Устанавливаем обновленный список
        _trophies.value = achievements
    }
}
