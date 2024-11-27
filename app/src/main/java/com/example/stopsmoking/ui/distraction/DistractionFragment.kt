package com.example.stopsmoking.ui.distraction

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.gridlayout.widget.GridLayout
import com.example.stopsmoking.R

class DistractionFragment : Fragment() {

    private lateinit var gridLayout: GridLayout
    private val images = listOf(
        R.drawable.smoking, R.drawable.smoke, R.drawable.smoke1, R.drawable.no_smoking,
        R.drawable.smoke_detector, R.drawable.no_smoking_sign
    )
    private var gameState = mutableListOf<CardState>()
    private var firstCardIndex: Int? = null
    private var secondCardIndex: Int? = null
    private var isProcessing = false

    // Define a sealed class for the card states
    sealed class CardState {
        data object Hidden : CardState()
        data class Revealed(val imageResId: Int) : CardState()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_distraction, container, false)
        gridLayout = view.findViewById(R.id.gridLayout)
        initializeGame()
        return view
    }

    private fun initializeGame() {
        // Удваиваем список картинок для пар и перемешиваем
        val cards = (images + images).shuffled()
        gameState = MutableList(cards.size) { CardState.Hidden } // Все карты изначально закрыты

        for (i in cards.indices) {
            val button = Button(requireContext())
            button.layoutParams = GridLayout.LayoutParams().apply {
                width = resources.getDimensionPixelSize(R.dimen.card_width)
                height = resources.getDimensionPixelSize(R.dimen.card_height)
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            }
            button.setBackgroundResource(R.drawable.card_back)

            button.setOnClickListener { onCardClicked(i, cards[i], button) }
            gridLayout.addView(button)
        }
    }

    private fun onCardClicked(index: Int, cardImage: Int, button: Button) {
        if (isProcessing || gameState[index] is CardState.Revealed) return

        // Animate the card opening
        val scaleX = ObjectAnimator.ofFloat(button, "scaleX", 0.3f, 0.6f)
        val scaleY = ObjectAnimator.ofFloat(button, "scaleY", 0.2f, 0.3f)
        scaleX.duration = 200
        scaleY.duration = 200
        scaleX.start()
        scaleY.start()

        // Открываем карту
        gameState[index] = CardState.Revealed(cardImage)
        button.setBackgroundResource(cardImage)

        if (firstCardIndex == null) {
            // Запоминаем первую открытую карту
            firstCardIndex = index
        } else {
            // Запоминаем вторую карту и проверяем
            secondCardIndex = index
            isProcessing = true
            checkMatch()
        }
    }

    private fun checkMatch() {
        val firstIndex = firstCardIndex!!
        val secondIndex = secondCardIndex!!

        Handler(Looper.getMainLooper()).postDelayed({
            if ((gameState[firstIndex] as CardState.Revealed).imageResId == (gameState[secondIndex] as CardState.Revealed).imageResId) {
                // Карты совпали
                Toast.makeText(requireContext(), "Пара найдена!", Toast.LENGTH_SHORT).show()
            } else {
                // Карты не совпали, закрываем их
                val firstButton = gridLayout.getChildAt(firstIndex) as Button
                val secondButton = gridLayout.getChildAt(secondIndex) as Button

                val scaleX = ObjectAnimator.ofFloat(firstButton, "scaleX", 0.8f, 1f)
                val scaleY = ObjectAnimator.ofFloat(firstButton, "scaleY", 0.8f, 1f)
                scaleX.duration = 200
                scaleY.duration = 200
                scaleX.start()
                scaleY.start()

                val scaleX1 = ObjectAnimator.ofFloat(secondButton, "scaleX", 0.8f, 1f)
                val scaleY1 = ObjectAnimator.ofFloat(secondButton, "scaleY", 0.8f, 1f)
                scaleX1.duration = 200
                scaleY1.duration = 200
                scaleX1.start()
                scaleY1.start()

                firstButton.setBackgroundResource(R.drawable.card_back)
                secondButton.setBackgroundResource(R.drawable.card_back)
                gameState[firstIndex] = CardState.Hidden
                gameState[secondIndex] = CardState.Hidden
            }
            firstCardIndex = null
            secondCardIndex = null
            isProcessing = false

            if (gameState.all { it is CardState.Revealed }) {
                // Все пары найдены
                Toast.makeText(requireContext(), "Вы выиграли!", Toast.LENGTH_LONG).show()
            }
        }, 500) // Задержка перед закрытием/оставлением карт
    }
}
