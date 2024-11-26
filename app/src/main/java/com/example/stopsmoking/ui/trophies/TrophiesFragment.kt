import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.stopsmoking.R

class TrophiesFragment : Fragment() {

    private val trophiesViewModel: TrophiesViewModel by activityViewModels()
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_trophies, container, false)
        listView = view.findViewById(R.id.trophiesListView)

        // Инициализация адаптера
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, mutableListOf())
        listView.adapter = adapter

        // Подписка на изменения в ViewModel
        trophiesViewModel.trophies.observe(viewLifecycleOwner, Observer { trophies ->
            adapter.clear()
            adapter.addAll(trophies)
            adapter.notifyDataSetChanged()
        })

        return view
    }
}
