import androidx.lifecycle.ViewModel

class ProgressViewModel : ViewModel() {

    private var progressBalance: Int = 0

    fun getProgressBalance(): Int {
        return progressBalance
    }

    fun setProgressBalance(balance: Int) {
        progressBalance = balance
    }

}
