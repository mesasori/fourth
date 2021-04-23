package com.example.fourth.activities

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.fourth.BaseActivity
import com.example.fourth.MainActivity
import com.example.fourth.R
import com.example.fourth.models.Constants
import com.example.fourth.ui.settings.FriendsActivity
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.concurrent.schedule

class SplashScreen : Activity() {

    lateinit var currentId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val myRef = this.getSharedPreferences(Constants.KEY, MODE_PRIVATE)
        val usersList = mutableListOf<String>()
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