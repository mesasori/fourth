package com.example.fourth.ui.settings

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.fourth.BaseActivity
import com.example.fourth.MainActivity
import com.example.fourth.R
import com.example.fourth.firebase.FirestoreClass
import com.example.fourth.models.Constants
import com.example.fourth.models.Constants.FIRESTORE_BIRTH
import com.example.fourth.models.Constants.FIRESTORE_IMAGE
import com.example.fourth.models.Constants.FIRESTORE_NAME
import com.example.fourth.models.Constants.FIRESTORE_PHONE
import com.example.fourth.models.Constants.FIRESTORE_SURNAME
import com.example.fourth.models.LoggedUserInfo
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.fragment_settings.view.*
import java.io.IOException

class EditProfile : BaseActivity(), View.OnClickListener {


    private var user: LoggedUserInfo ?= null
    private var imageUri: Uri ?= null
    private var imageURL: String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        user = intent.extras?.getParcelable("user")
        filling(user!!)

        et_editP_email.editText?.keyListener = null

        bt_backToSetting.setOnClickListener(this)
        bt_saveEdited.setOnClickListener(this)
        bt_editP_avatar.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.bt_backToSetting -> onBackPressed()
            R.id.bt_saveEdited -> {
                saveInfo()

                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            }
            R.id.bt_editP_avatar -> {
                if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {

                    Constants.showImageChooser(this)

                } else {
                    ActivityCompat.requestPermissions(this,
                            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_PERMISSIONS)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_CODE) {
                if (data != null) {
                    try {
                        imageUri = data.data!!
                        FirestoreClass().uploadImageToCloud(this, imageUri)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(this, resources.getString(R.string.image_select_failed), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                showErrorSnackBar("Permission is granted", false)

            else Toast.makeText(this, "Permission is denied. You can also apply it from settings", Toast.LENGTH_LONG).show()
        }
    }

    private fun saveInfo() {
        val document = FirebaseFirestore.getInstance().collection("users").document(user?.id!!)
        FirebaseFirestore.getInstance().runBatch { it ->
            it.update(document, FIRESTORE_SURNAME, et_editP_surname.editText?.text.toString().trim { it <= ' ' })
            it.update(document, FIRESTORE_BIRTH, et_editP_birth.editText?.text.toString().trim { it <= ' ' })
            it.update(document, FIRESTORE_PHONE, et_editP_phone.editText?.text.toString().trim { it <= ' ' })
            it.update(document, FIRESTORE_NAME, et_editP_name.editText?.text.toString().trim { it <= ' ' })
            if (imageURL != null ) {
                it.update(document, FIRESTORE_IMAGE, imageURL)
                user?.image = imageURL.toString()
            }
        }
        user?.name = et_editP_name.editText?.text.toString().trim{ it <= ' '}
        user?.surname = et_editP_surname.editText?.text.toString().trim{ it <= ' '}
        user?.birth = et_editP_birth.editText?.text.toString().trim{ it <= ' '}
        user?.phone = et_editP_phone.editText?.text.toString().trim{ it <= ' '}
    }
    private fun filling(user: LoggedUserInfo) {
        Glide.with(this).load(user.image).into(iv_edit_setImage)
        et_editP_email.editText?.setText(user.email)
        et_editP_name.editText?.setText(user.name)
        et_editP_surname.editText?.setText(user.surname)
        et_editP_birth.editText?.setText(user.birth)
        et_editP_phone.editText?.setText(user.phone)
    }

    fun uploadImage(url: String) {
        imageURL = url
        Toast.makeText(this, imageURL, Toast.LENGTH_LONG).show()
    }
}