package com.example.fourth.activities

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.fourth.BaseActivity
import com.example.fourth.MainActivity
import com.example.fourth.R
import com.example.fourth.models.Constants
import com.example.fourth.models.Constants.FIRESTORE_EMAIL
import com.example.fourth.models.Constants.FIRESTORE_SURNAME
import com.example.fourth.models.LoggedUserInfo
import com.example.fourth.ui.settings.SettingsFragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_authorization.*

class Authorization : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)
        create_new_account.setOnClickListener {
            val intent = Intent(this, Registration::class.java)
            startActivity(intent)
        }

        tv_forgotPassword.setOnClickListener {
            val intent = Intent(this@Authorization, ForgotPassword::class.java)
            startActivity(intent)
        }

    }


    private fun validateRegister(): Boolean {
        return when {
            TextUtils.isEmpty(et_emailAuth.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Вы забыли ввести email", true)
                false
            }
            TextUtils.isEmpty(et_passwordAuth.text.toString().trim { it <= ' ' }) -> {                                //Проверка на пустые поля
                showErrorSnackBar("Вы забыли ввести пароль", true)
                false
            }
            else -> {
                true
            }
        }
    }

    fun loginRegisteredUsers(view: View) {
        if (validateRegister()) {
            showProgressDialog(animation_lottie)

            val email: String = et_emailAuth.text.toString().trim { it <= ' ' }
            val password: String = et_passwordAuth.text.toString().trim { it <= ' ' }
            val myRef = this.getSharedPreferences(Constants.KEY, MODE_PRIVATE)
            val editor = myRef.edit()
            var userInfo = ""

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->                                                                //Проверка на lohin and password

                    hideProgressDialog(animation_lottie)

                    if (task.isSuccessful) {
                        val userId = task.result!!.user!!.uid
                        editor.putString(Constants.USER_ID, userId).apply()
                        FirebaseFirestore.getInstance().collection("users")
                                .document(userId).get().addOnCompleteListener { data ->
                                    if (data.isSuccessful) {
                                        userInfo = "${data.result?.getString(Constants.FIRESTORE_NAME)} " +
                                                "${data.result?.getString(FIRESTORE_SURNAME)} " +
                                                "${data.result?.getString(FIRESTORE_EMAIL)}"
                                        Toast.makeText(this, userInfo, Toast.LENGTH_LONG).show()
                                        editor.putString(Constants.USER_FULL_INFO, userInfo).apply()
                                    } else editor.putString(Constants.USER_FULL_INFO, "Error").apply()
                                }
                        showErrorSnackBar("Вы успешно вошли", false)

                        val intent = Intent(this@Authorization, MainActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else {
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }

        } else validateRegister()
    }
}


