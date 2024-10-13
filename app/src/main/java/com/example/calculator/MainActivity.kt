package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var resultTextView: TextView
    private var currentInput = ""
    private var currentOperator = ""
    private var firstOperand = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Tìm TextView hiển thị kết quả
        resultTextView = findViewById<TextView>(R.id.result_text)

        setupNumberButtons()
        setupOperatorButtons()
        setupSpecialButtons()
    }

    private fun setupNumberButtons() {
        val numberButtons = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
        for (number in numberButtons) {
            val buttonId = resources.getIdentifier("button_$number", "id", packageName)
            findViewById<Button>(buttonId).setOnClickListener {
                if (currentInput.length < 9) {  // Giới hạn độ dài đầu vào
                    currentInput += number
                    updateDisplay()
                }
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
                if (currentInput.isNotEmpty()) {
                    if (currentOperator.isEmpty()) {
                        firstOperand = currentInput.toInt()
                        currentOperator = operator
                        currentInput = ""
                    } else {
                        calculate()
                        currentOperator = operator
                    }
                }
            }
        }

        findViewById<Button>(R.id.button_equals).setOnClickListener {
            if (currentInput.isNotEmpty() && currentOperator.isNotEmpty()) {
                calculate()
                currentOperator = ""
            }
        }
    }

    private fun setupSpecialButtons() {
        // CE button
        findViewById<Button>(R.id.button_ce).setOnClickListener {
            currentInput = ""
            updateDisplay()
        }

        // C button
        findViewById<Button>(R.id.button_c).setOnClickListener {
            currentInput = ""
            currentOperator = ""
            firstOperand = 0
            updateDisplay()
        }

        // BS button
        findViewById<Button>(R.id.button_bs).setOnClickListener {
            if (currentInput.isNotEmpty()) {
                currentInput = currentInput.dropLast(1)
                updateDisplay()
            }
        }

        // +/- button
        findViewById<Button>(R.id.button_sign).setOnClickListener {
            if (currentInput.isNotEmpty()) {
                currentInput = if (currentInput.startsWith("-")) {
                    currentInput.substring(1)
                } else {
                    "-$currentInput"
                }
                updateDisplay()
            }
        }
    }

    private fun calculate() {
        if (currentInput.isNotEmpty()) {
            val secondOperand = currentInput.toInt()
            val result = when (currentOperator) {
                "+" -> firstOperand + secondOperand
                "-" -> firstOperand - secondOperand
                "x" -> firstOperand * secondOperand
                "/" -> if (secondOperand != 0) firstOperand / secondOperand else 0
                else -> secondOperand
            }
            currentInput = result.toString()
            updateDisplay()
            firstOperand = result
        }
    }

    private fun updateDisplay() {
        resultTextView.text = if (currentInput.isEmpty()) "0" else currentInput
    }
}