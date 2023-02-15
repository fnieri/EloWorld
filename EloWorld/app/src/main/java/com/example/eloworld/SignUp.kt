package com.example.eloworld

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class SignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up)
    }

    fun signUp_SU(v: View?) {
        //https://stackoverflow.com/questions/4531396/get-value-of-a-edit-text-field
        val usernameField = findViewById<TextInputEditText>(R.id.username_input_edit)
        val username = usernameField.getText().toString()

        val firstPasswordField = findViewById<TextInputEditText>(R.id.password_input_edit)
        val firstPassword = firstPasswordField.getText().toString()

        if (firstPassword.length < 4) {
            firstPasswordField.setError("Password length must be greater than 4")
        }
        val confirmPasswordField = findViewById<TextInputEditText>(R.id.password_confirm_edit)
        val confirmPassword = confirmPasswordField.getText().toString()

        if (firstPassword != confirmPassword) {
            confirmPasswordField.setError("Passwords must be equal")
        }
        println(username + firstPassword)

        if (firstPassword == confirmPassword) {
            finish()
            Util.changeLayout(this, BottomNav::class.java)
        }

    }

    fun checkEmptyString(s: String?) {

    }


    fun changeLayout_SU(v: View?) {
        finish()    // End current activity and switch to SignIn screen
        Util.changeLayout(this, SignIn::class.java)
    }

}