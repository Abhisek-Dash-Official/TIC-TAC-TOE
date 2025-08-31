package com.abhisekdash.tic_tac_toe

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.os.Handler
import android.os.Looper
import android.widget.TextView

class Game : AppCompatActivity() {
    // 0 = O, 1 = X, 2 = empty
    var currentPlayer = if ((0..1).random() == 0) 0 else 1
    var gameState = MutableList(9) { 2 }

    private lateinit var statusDisplay: TextView
    val winPositions = arrayOf(
        intArrayOf(0, 1, 2),
        intArrayOf(3, 4, 5),
        intArrayOf(6, 7, 8),
        intArrayOf(0, 3, 6),
        intArrayOf(1, 4, 7),
        intArrayOf(2, 5, 8),
        intArrayOf(0, 4, 8),
        intArrayOf(2, 4, 6)
    )
    private val cellIds = arrayOf(
        R.id.gridElement0,
        R.id.gridElement1,
        R.id.gridElement2,
        R.id.gridElement3,
        R.id.gridElement4,
        R.id.gridElement5,
        R.id.gridElement6,
        R.id.gridElement7,
        R.id.gridElement8
    )

    private fun resetGame(){
        currentPlayer =   if (currentPlayer == 0) 1 else 0
        gameState = MutableList(9) { 2 }
        val statusText = if (currentPlayer == 0) "O" else "X"
        statusDisplay.text = "Player $statusText's Turn"
        for (id in cellIds) {
            val cell = findViewById<ImageView>(id)
            cell.setImageResource(0)
        }
    }

    fun checkDraw(): Boolean {
        if (gameState.contains(2)) {
            return false
        }

        statusDisplay.text = "It's a Draw!"
        Toast.makeText(this, "Opps!! It's a Draw!", Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed({
            resetGame()
        }, 2000)
        return true
    }
    fun checkWinner(): Boolean {
        for (pos in winPositions) {
            if (gameState[pos[0]] == gameState[pos[1]] &&
                gameState[pos[1]] == gameState[pos[2]] &&
                gameState[pos[0]] != 2
            ) {
                val winner = if (gameState[pos[0]] == 0) "O" else "X"

                // Display Winner
                Toast.makeText(this, "$winner Wins!", Toast.LENGTH_SHORT).show()
                val statusTextView = statusDisplay
                statusTextView.text = "$winner Wins!"
                Handler(Looper.getMainLooper()).postDelayed({
                    resetGame()
                }, 2000)

                return true
            }
        }
        return false
    }

    fun computerMove() {
        // find all empty cells
        val emptyCells = gameState.mapIndexed { index, value -> if (value == 2) index else null }
            .filterNotNull()

        if (emptyCells.isNotEmpty()) {
            val choice = emptyCells.random() // random cell
            gameState[choice] = currentPlayer

            // Find ImageView by tag
            val gridCell = findViewById<ImageView>(cellIds[choice])

            if (currentPlayer == 0) {
                gridCell.setImageResource(R.drawable.o)
            } else {
                gridCell.setImageResource(R.drawable.x)
            }

            // Check Draw or winner
            if(checkDraw())return
            if (checkWinner()) return

            // Switch back to player
            currentPlayer = if (currentPlayer == 0) 1 else 0
            val statusText = if (currentPlayer == 0) "O" else "X"
            statusDisplay.text = "Player $statusText's Turn"
        }
    }
    fun playerClick(view: View) {
        val img = view as ImageView
        val tappedIndex = img.tag.toString().toInt()

        // If cell is empty
        if (gameState[tappedIndex] == 2) {
            // Mark player move
            gameState[tappedIndex] = currentPlayer

            if (currentPlayer == 0) {
                img.setImageResource(R.drawable.o) // put o.png
            } else {
                img.setImageResource(R.drawable.x) // put x.png
            }

            // Check Draw or winner
            if(checkDraw())return
            if (checkWinner()) return

            // Switch to computer
            currentPlayer = if (currentPlayer == 0) 1 else 0
            val statusText = if (currentPlayer == 0) "O" else "X"
            statusDisplay.text = "Computer $statusText's Turn"
            Handler(Looper.getMainLooper()).postDelayed({
                computerMove()
            }, 700)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game)

        // Initialize status display
        statusDisplay = findViewById<TextView>(R.id.statusDisplay)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}