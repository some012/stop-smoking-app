import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.stopsmoking.R
import com.example.stopsmoking.ui.rewards.RewardsViewModel

class RewardsFragment : Fragment() {

    private val rewardsViewModel: RewardsViewModel by activityViewModels()
    private val progressViewModel: ProgressViewModel by activityViewModels()

    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var tvBalance: TextView
    private var balance: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rewards, container, false)

        listView = view.findViewById(R.id.rewardsListView)
        tvBalance = view.findViewById(R.id.tv_balance)
        val rewardInput: EditText = view.findViewById(R.id.rewardInput)
        val rewardCostInput: EditText = view.findViewById(R.id.rewardCostInput)
        val addRewardButton: Button = view.findViewById(R.id.addRewardButton)
        val resetButton: Button = view.findViewById(R.id.resetButton)

        // Загрузка данных из ViewModel
        rewardsViewModel.loadData(requireContext())
        balance = rewardsViewModel.getRewardsBalance()
        tvBalance.text = "Баланс: $balance руб."

        // Инициализация адаптера
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, mutableListOf())
        listView.adapter = adapter

        // Обновление списка наград
        adapter.addAll(rewardsViewModel.getRewards())

        addRewardButton.setOnClickListener {
            val reward = rewardInput.text.toString()
            val cost = rewardCostInput.text.toString().toIntOrNull()

            if (reward.isNotBlank() && cost != null) {
                if (balance >= cost) {
                    // Добавляем награду
                    val rewardText = "$reward - $cost руб."
                    rewardsViewModel.addReward(rewardText)
                    balance -= cost
                    tvBalance.text = "Баланс: $balance руб."
                    rewardsViewModel.setProgressBalance(balance)
                    rewardsViewModel.saveData(requireContext()) // Сохраняем изменения
                    adapter.add(rewardText)
                    rewardInput.text.clear()
                    rewardCostInput.text.clear()
                } else {
                    rewardInput.error = "Недостаточно средств!"
                }
            }
        }

        resetButton.setOnClickListener {
            rewardsViewModel.clearRewards() // Очищаем награды
            balance = progressViewModel.getProgressBalance() // Получаем текущий баланс
            rewardsViewModel.setProgressBalance(balance)
            tvBalance.text = "Баланс: $balance руб." // Обновляем текст баланса
            adapter.clear() // Очищаем адаптер
            rewardsViewModel.saveData(requireContext()) // Сохраняем изменения
        }

        return view
    }
}
