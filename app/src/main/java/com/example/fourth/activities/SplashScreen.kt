package com.example.fourth.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.fourth.MainActivity
import com.example.fourth.R
import com.example.fourth.models.Constants
import java.util.*
import kotlin.concurrent.schedule

class SplashScreen : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val myRef = this.getSharedPreferences(Constants.KEY, MODE_PRIVATE)
        Timer().schedule(1500){
            if(myRef.contains(Constants.USER_ID)) {
                val intent = Intent(this@SplashScreen, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this@SplashScreen, Authorization::class.java)
                startActivity(intent)
                finish()
            }
        }

    }
}