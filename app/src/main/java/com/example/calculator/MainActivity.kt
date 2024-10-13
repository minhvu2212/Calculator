package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var resultTextView: TextView
    private var currentExpression = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultTextView = findViewById(R.id.result_text)

        setupNumberButtons()
        setupOperatorButtons()
        setupSpecialButtons()
    }

    private fun setupNumberButtons() {
        val numberButtons = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
        for (number in numberButtons) {
            val buttonId = resources.getIdentifier("button_$number", "id", packageName)
            findViewById<Button>(buttonId).setOnClickListener {
                appendToExpression(number)
            }
        }
    }

    private fun setupOperatorButtons() {
        val operators = mapOf(
            "button_plus" to "+",
            "button_minus" to "-",
            "button_multiply" to "x",
            "button_divide" to "/"
        )

        for ((buttonName, operator) in operators) {
            val buttonId = resources.getIdentifier(buttonName, "id", packageName)
            findViewById<Button>(buttonId).setOnClickListener {
                if (currentExpression.isNotEmpty() && !currentExpression.endsWith(operator)) {
                    appendToExpression(operator)
                }
            }
        }

        findViewById<Button>(R.id.button_equals).setOnClickListener {
            if (currentExpression.isNotEmpty()) {
                calculate()
            }
        }
    }

    private fun setupSpecialButtons() {
        findViewById<Button>(R.id.button_ce).setOnClickListener {
            clearEntry()
        }

        findViewById<Button>(R.id.button_c).setOnClickListener {
            clearAll()
        }

        findViewById<Button>(R.id.button_bs).setOnClickListener {
            backspace()
        }

        findViewById<Button>(R.id.button_sign).setOnClickListener {
            changeSign()
        }

        findViewById<Button>(R.id.button_dot).setOnClickListener {
            appendDecimalPoint()
        }
    }

    private fun appendToExpression(value: String) {
        currentExpression += value
        updateDisplay()
    }

    private fun clearEntry() {
        val lastOperatorIndex = currentExpression.indexOfLast { it in setOf('+', '-', 'x', '/') }
        if (lastOperatorIndex != -1) {
            currentExpression = currentExpression.substring(0, lastOperatorIndex + 1)
        } else {
            currentExpression = ""
        }
        updateDisplay()
    }

    private fun clearAll() {
        currentExpression = ""
        updateDisplay()
    }

    private fun backspace() {
        if (currentExpression.isNotEmpty()) {
            currentExpression = currentExpression.dropLast(1)
            updateDisplay()
        }
    }

    private fun changeSign() {
        if (currentExpression.isNotEmpty()) {
            val lastOperatorIndex = currentExpression.indexOfLast { it in setOf('+', '-', 'x', '/') }
            if (lastOperatorIndex == -1) {
                currentExpression = if (currentExpression.startsWith("-")) {
                    currentExpression.substring(1)
                } else {
                    "-$currentExpression"
                }
            } else {
                val lastNumber = currentExpression.substring(lastOperatorIndex + 1)
                if (lastNumber.isNotEmpty()) {
                    currentExpression = currentExpression.substring(0, lastOperatorIndex + 1) +
                            if (lastNumber.startsWith("-")) lastNumber.substring(1) else "-$lastNumber"
                }
            }
            updateDisplay()
        }
    }

    private fun appendDecimalPoint() {
        val lastOperatorIndex = currentExpression.indexOfLast { it in setOf('+', '-', 'x', '/') }
        val lastNumber = if (lastOperatorIndex == -1) currentExpression else currentExpression.substring(lastOperatorIndex + 1)
        if (!lastNumber.contains(".")) {
            currentExpression += if (lastNumber.isEmpty()) "0." else "."
            updateDisplay()
        }
    }

    private fun calculate() {
        try {
            val result = evaluateExpression(currentExpression)
            currentExpression = result.toString()
            updateDisplay()
        } catch (e: Exception) {
            resultTextView.text = "Error"
        }
    }

    private fun evaluateExpression(expression: String): Double {
        val parts = expression.replace("x", "*").split("+", "-", "*", "/")
        val operators = expression.replace("x", "*").filter { it in setOf('+', '-', '*', '/') }

        var result = parts[0].toDouble()
        for (i in operators.indices) {
            when (operators[i]) {
                '+' -> result += parts[i + 1].toDouble()
                '-' -> result -= parts[i + 1].toDouble()
                '*' -> result *= parts[i + 1].toDouble()
                '/' -> result /= parts[i + 1].toDouble()
            }
        }
        return result
    }

    private fun updateDisplay() {
        resultTextView.text = if (currentExpression.isEmpty()) "0" else currentExpression
    }
}