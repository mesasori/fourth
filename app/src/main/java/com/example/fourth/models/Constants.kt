package com.example.fourth.models

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

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
    const val CLOUD_IMAGE = "user_profile_image"

    const val DATABASE_FRIENDS = "friends"
    const val DATABASE_ID = "id"
    const val DATABASE_MESSAGES = "messages"
    const val DATABASE_USERS = "users"

    const val EXTRAS_USERS = "userList"

    fun showImageChooser(activity: Activity) {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(galleryIntent, Constants.PICK_IMAGE_CODE)
    }

    fun getFileExtension(activity: Activity, uri: Uri?) = MimeTypeMap.getSingleton().getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
}