package com.example.fourth.firestore

import android.util.Log
import com.example.fourth.activities.Registration
import com.example.fourth.models.LoggedUserInfo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FirestoreClass {
    private val myFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: Registration, info: LoggedUserInfo) {
        myFireStore.collection("users")
            .document(info.id!!)
            .set(info, SetOptions.merge())
            .addOnCompleteListener {
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener { e ->
                activity.userRegistrationFailed()
                Log.e(activity.javaClass.simpleName, "Ошибка во время регистрации")
            }
    }

}