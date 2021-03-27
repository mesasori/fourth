package com.example.fourth.ui.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.fourth.BaseActivity
import com.example.fourth.MainActivity
import com.example.fourth.R
import com.example.fourth.models.Constants
import com.example.fourth.models.LoggedUserInfo
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfile : BaseActivity(), View.OnClickListener {

    var myRef: SharedPreferences? = null
    var userId: String? = null
    private var user: LoggedUserInfo ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        user = intent.extras?.getParcelable("user")
        filling(user!!)

        et_editP_email.editText?.keyListener = null

        myRef = application?.getSharedPreferences(Constants.KEY, Context.MODE_PRIVATE)
        userId = myRef?.getString(Constants.USER_ID, "none")
        bt_backToSetting.setOnClickListener(this)

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

            }
        }
    }
    private fun saveInfo() {
        val document = FirebaseFirestore.getInstance().collection("users").document(userId!!)
        FirebaseFirestore.getInstance().runBatch { it ->
            it.update(document, "surname", et_editP_surname.editText?.text.toString().trim{ it <= ' '})
            it.update(document, "dateOfBirth", et_editP_birth.editText?.text.toString().trim{ it <= ' '})
            it.update(document, "mobile", et_editP_phone.editText?.text.toString().trim{ it <= ' '})
            it.update(document, "firstName", et_editP_name.editText?.text.toString().trim{ it <= ' '})
        }
        user?.name = et_editP_name.editText?.text.toString().trim{ it <= ' '}
        user?.surname = et_editP_surname.editText?.text.toString().trim{ it <= ' '}
        user?.birth = et_editP_birth.editText?.text.toString().trim{ it <= ' '}
        user?.phone = et_editP_phone.editText?.text.toString().trim{ it <= ' '}
    }
    private fun filling(user: LoggedUserInfo) {
        et_editP_email.editText?.setText(user.email)
        et_editP_name.editText?.setText(user.name)
        et_editP_surname.editText?.setText(user.surname)
        et_editP_birth.editText?.setText(user.birth)
        et_editP_phone.editText?.setText(user.phone)
    }
}