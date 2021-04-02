package com.example.fourth.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

enum class AppState(val state: String) {
    ONLINE("Online"),
    OFFLINE("Offline"),
    TYPING("Typing");

    companion object {
        fun updateState(appState: AppState) {
            val currentId = FirebaseAuth.getInstance().currentUser?.uid.toString()
            //database.setValue(appState)
        }
    }
}