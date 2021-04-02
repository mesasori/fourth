package com.example.fourth.firebase

import android.app.Activity
import android.net.Uri
import android.util.Log
import com.example.fourth.activities.Registration
import com.example.fourth.models.Constants
import com.example.fourth.models.LoggedUserInfo
import com.example.fourth.ui.settings.EditProfile
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage

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

    fun uploadImageToCloud(activity: Activity, imageURI: Uri?) {
        val storageReference = FirebaseStorage.getInstance().reference.child(
            Constants.CLOUD_IMAGE + System.currentTimeMillis() + "." +
                    Constants.getFileExtension(activity, imageURI)
        )
        storageReference.putFile(imageURI!!).addOnSuccessListener { task ->
            Log.e("Firebase image uri", task.metadata?.reference?.downloadUrl.toString())

            task.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                if (activity is EditProfile) {
                    activity.uploadImage(it.toString())
                }
                Log.e("Downloadable image uri", it.toString())
            }
        }
            .addOnFailureListener {
                Log.e(activity.javaClass.simpleName, it.message, it)
            }
    }


}