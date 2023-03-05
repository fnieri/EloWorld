package com.example.eloworld

import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity

object Util {
    fun changeLayout(caller: android.content.Context, newScreen: Class<*>) {
        val intent = Intent(caller, newScreen)
        // End current activity and switch to SignUp screen
        caller.startActivity(intent)
    }
}
