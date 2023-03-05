package com.example.eloworld

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.example.eloworld.Util

class SignIn : AppCompatActivity() {


    // Sign up button on click
    fun changeLayout_SI(v: View?) {
        finish()    // End current activity and switch to SignUp screen
        Util.changeLayout(this, SignUp::class.java)
    }

    fun signIn_SI(v: View?) {
        val usernameField = findViewById<TextInputEditText>(R.id.username_input_edit)
        val username = usernameField.text.toString()

        val passwordField = findViewById<TextInputEditText>(R.id.password_input_edit)
        val password = passwordField.text.toString()
        println(username + password)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_in)
    }
}
