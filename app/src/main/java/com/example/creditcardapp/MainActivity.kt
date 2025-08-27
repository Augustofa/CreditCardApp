package com.example.creditcardapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        formatFields()
    }

    fun formatFields() {
        formatNumberField()
        formatNameField()
        formatDateField()
    }

    fun formatNumberField() {
        val numberField = findViewById<EditText>(R.id.cardNumberView)

        numberField.addTextChangedListener(object : TextWatcher {
            private var formatting = false
            override fun afterTextChanged(s: Editable?) {
                if (formatting) return
                formatting = true

                val digits = s.toString().replace(" ", "")
                val formatted = digits.chunked(4).joinToString(" ")
                numberField.setText(formatted)
                numberField.setSelection(formatted.length)

                formatting = false
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        numberField.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val digits = numberField.text.toString().replace(" ", "")
                if (digits.length != 16) {
                    numberField.error = "Número inválido"
                } else {
                    numberField.error = null
                }
            }
        }
    }

    fun formatNameField() {
        val nameField = findViewById<EditText>(R.id.cardNameView)

        nameField.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val name = nameField.text.toString().trim()
                if (name.length < 3) {
                    nameField.error = "Nome muito curto"
                } else {
                    nameField.error = null
                }
            }
        }
    }

    fun formatDateField() {
        val dateField = findViewById<EditText>(R.id.cardExpirationView)

        dateField.addTextChangedListener(object : TextWatcher {
            private var formatting = false
            override fun afterTextChanged(s: Editable?) {
                if (formatting) return
                formatting = true

                val digits = s.toString().replace("/", "")
                var formatted = digits

                if (digits.length > 2) {
                    formatted = digits.substring(0, 2) + "/" +
                            digits.substring(2, minOf(4, digits.length))
                }

                dateField.setText(formatted)
                dateField.setSelection(formatted.length)

                formatting = false
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        dateField.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val text = dateField.text.toString()
                if (!isValidDate(text)) {
                    dateField.error = "Data inválida"
                } else {
                    dateField.error = null
                }
            }
        }
    }

    private fun isValidDate(date: String): Boolean {
        return try {
            if (date.length != 5) return false
            val sdf = SimpleDateFormat("MM/yy", Locale.getDefault())
            sdf.isLenient = false
            sdf.parse(date) != null
        } catch (e: Exception) {
            false
        }
    }
}