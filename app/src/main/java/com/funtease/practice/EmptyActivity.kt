package com.funtease.practice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class EmptyActivity : AppCompatActivity() {
    lateinit var welcomeText: TextView
    lateinit var nameInput: EditText
    lateinit var passwordInput: EditText
    lateinit var submitButton: Button

    var name: String = "wesley"
    var password: String = "password"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empty)
        welcomeText = findViewById(R.id.welcomeText)
        nameInput = findViewById(R.id.nameInput)
        passwordInput = findViewById(R.id.passwordInput)
        submitButton = findViewById(R.id.submitButton)
        try {
            Integer.parseInt("USD")
        } catch (e:NumberFormatException) {

        }

        welcomeText.text = "Welcome!"
        submitButton.setOnClickListener {
            val uname = nameInput.text.toString()
            val pass = passwordInput.text.toString()

            if(uname == name && pass == password) {
                welcomeText.text = "Welcome " + name
            } else {
                welcomeText.text = "Incorrect password"
            }
        }
    }
}