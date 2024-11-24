package com.example.stopsmoking

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.stopsmoking.ui.distraction.DistractionFragment
import com.example.stopsmoking.ui.health.HealthFragment
import com.example.stopsmoking.ui.progress.ProgressFragment
import com.example.stopsmoking.ui.rewards.RewardsFragment
import com.example.stopsmoking.ui.trophies.TrophiesFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализация BottomNavigationView
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        // Устанавливаем начальный фрагмент
        if (savedInstanceState == null) {
            replaceFragment(ProgressFragment())
        }

        // Настраиваем обработку кликов на элементы меню
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_progress -> replaceFragment(ProgressFragment())
                R.id.nav_trophies -> replaceFragment(TrophiesFragment())
                R.id.nav_rewards -> replaceFragment(RewardsFragment())
                R.id.nav_health -> replaceFragment(HealthFragment())
                R.id.nav_distraction -> replaceFragment(DistractionFragment())
                else -> false
            }
        }
    }

    // Метод замены фрагментов
    private fun replaceFragment(fragment: Fragment): Boolean {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
        return true
    }
}
