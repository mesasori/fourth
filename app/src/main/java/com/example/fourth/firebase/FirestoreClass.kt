package com.example.fourth.firebase

import android.app.Activity
import android.net.Uri
import android.util.Log
import android.view.View
import com.example.fourth.activities.Registration
import com.example.fourth.models.Constants
import com.example.fourth.models.FriendsUser
import com.example.fourth.models.LoggedUserInfo
import com.example.fourth.ui.settings.EditProfile
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_edit_profile.*

class FirestoreClass {
    private val myFireStore = FirebaseFirestore.getInstance()
    val adminUser = FriendsUser("JShxl9yhuNgguhfWx0IqmRq390K2", "Administrator", "",
            "https://firebasestorage.googleapis.com/v0/b/fourth-45854.appspot.com/o/user_profile_image1618169578973.jpg?alt=media&token=0a71c640-07ea-4f36-8a88-3972d615f4df")

    fun registerUser(activity: Registration, info: LoggedUserInfo) {
        Firebase.database.reference.child(Constants.DATABASE_USERS).child(info.id!!).setValue(info)
        Firebase.database.reference.child(Constants.DATABASE_FRIENDS).child(info.id!!).child(adminUser.id!!).setValue(adminUser)
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
        if (activity is EditProfile) activity.animation_lottie.visibility = View.VISIBLE
        val storageReference = FirebaseStorage.getInstance().reference.child(
            Constants.CLOUD_IMAGE + System.currentTimeMillis() + "." +
                    Constants.getFileExtension(activity, imageURI)
        )
        storageReference.putFile(imageURI!!).addOnSuccessListener { task ->
            Log.e("Firebase image uri", task.metadata?.reference?.downloadUrl.toString())

            task.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                if (activity is EditProfile) {
                    activity.uploadImage(it.toString())
                    activity.iv_edit_setImage.setImageURI(imageURI)
                    Log.e("Downloadable image uri", it.toString())
                    activity.animation_lottie.visibility = View.INVISIBLE
                }

            }
        }
            .addOnFailureListener {
                Log.e(activity.javaClass.simpleName, it.message, it)
            }
    }


}