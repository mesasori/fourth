package com.example.fourth.models

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore

object Constants {
    const val USERS = "users"
    const val KEY = "TABLE"
    const val USER_ID = "user_id"
    const val USER_FULL_INFO = "user_full_info"
    const val READ_PERMISSIONS = 2
    const val PICK_IMAGE_CODE = 1
    const val FIRESTORE_NAME = "name"
    const val FIRESTORE_SURNAME = "surname"
    const val FIRESTORE_EMAIL = "email"
    const val FIRESTORE_PHONE = "phone"
    const val FIRESTORE_PASSWORD = "password"
    const val FIRESTORE_BIRTH = "birth"
    const val FIRESTORE_IMAGE = "image"

    fun showImageChooser(activity: Activity) {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(galleryIntent, Constants.PICK_IMAGE_CODE)
    }
}