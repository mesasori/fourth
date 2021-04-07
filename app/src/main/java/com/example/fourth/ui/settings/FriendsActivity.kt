package com.example.fourth.ui.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fourth.R
import com.example.fourth.models.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FriendsActivity : AppCompatActivity() {

    private lateinit var DATABASE: DatabaseReference
    private lateinit var AUTH: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)
        DATABASE = Firebase.database.reference
        AUTH = FirebaseAuth.getInstance()

        //var friendsList = mutableMapOf()



        DATABASE.child(Constants.DATABASE_FRIENDS).child(AUTH.currentUser!!.uid).setValue("use")
    }
}