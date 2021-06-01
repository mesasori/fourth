package com.example.fourth

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.fourth.models.Constants.DATABASE_FRIENDS
import com.example.fourth.models.Constants.DATABASE_USERS
import com.example.fourth.models.FriendsUser
import com.example.fourth.models.LoggedUserInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_friends.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mapOfUsers: MutableMap<String, FriendsUser> = mutableMapOf()
        Firebase.database.reference.child(DATABASE_USERS).get().addOnSuccessListener {
            for (i in it.children) {
                mapOfUsers[i.key.toString()] = FriendsUser(i.child("id").value.toString(),
                        i.child("name").value.toString(),
                        i.child("surname").value.toString(),
                        i.child("image").value.toString())
                Log.e("Afvsc", i.key.toString())
            }
            val doc = Firebase.database.reference.child(DATABASE_FRIENDS).child(FirebaseAuth.getInstance().currentUser!!.uid)
            Firebase.database.reference.child(DATABASE_FRIENDS).child(FirebaseAuth.getInstance().currentUser!!.uid)
                    .get().addOnSuccessListener {
                for (i in it.children)
                    doc.child(i.key.toString()).setValue(mapOfUsers[i.key.toString()])
            }
        }




        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)

        navView.setupWithNavController(navController)
    }
}