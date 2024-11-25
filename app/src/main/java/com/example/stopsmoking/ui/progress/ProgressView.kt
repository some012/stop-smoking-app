import android.content.Context
import androidx.lifecycle.ViewModel

class ProgressViewModel : ViewModel() {

    private var progressBalance: Int = 0

    // Метод для получения текущего баланса
    fun getProgressBalance(): Int {
        return progressBalance
    }

    // Метод для установки баланса
    fun setProgressBalance(balance: Int) {
        progressBalance = balance
    }

    // Метод для сохранения данных в SharedPreferences
    fun saveData(context: Context) {
        val sharedPreferences = context.getSharedPreferences("ProgressData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Сохраняем баланс
        editor.putInt("progress_balance", progressBalance)
        editor.apply()
    }

    // Метод для загрузки данных из SharedPreferences
    fun loadData(context: Context) {
        val sharedPreferences = context.getSharedPreferences("ProgressData", Context.MODE_PRIVATE)

        // Загружаем баланс
        progressBalance = sharedPreferences.getInt("progress_balance", 0)
    }
}
