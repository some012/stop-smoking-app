package com.example.stopsmoking.ui.trophies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stopsmoking.R

class TrophiesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TrophiesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_trophies, container, false)
        recyclerView = view.findViewById(R.id.trophiesRecyclerView)

        adapter = TrophiesAdapter(getTrophies())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        return view
    }

    private fun getTrophies(): List<String> {
        // Пример трофеев
        return listOf(
            "1 день без курения",
            "Сэкономлено 1000 рублей",
            "1 неделя без сигарет"
        )
    }
}
