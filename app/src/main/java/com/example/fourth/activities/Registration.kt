package com.example.fourth.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.example.fourth.BaseActivity
import com.example.fourth.R
import com.example.fourth.firestore.FirestoreClass
import com.example.fourth.models.LoggedUserInfo
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_registration.*

class Registration : BaseActivity() {
    private lateinit var firebaseUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        tv_have_an_account.setOnClickListener {
            val intent = Intent(this, Authorization::class.java)
            startActivity(intent)
            finish()
        }
        create_account.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        if (validateRegister()) {
            showProgressDialog(animation_lottie)

            val email: String = email_registration.text.toString().trim { it <= ' ' }
            val password: String = password_registration.text.toString().trim { it <= ' ' }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->



                        if (task.isSuccessful) {
                            firebaseUser = task.result!!.user!!

                            val user = LoggedUserInfo(
                                firebaseUser.uid,
                                et_firstName.text.toString(),
                                et_surname.text.toString(),
                                email_registration.text.toString(), "",
                                password_registration.text.toString()
                            )
                            FirestoreClass().registerUser(this@Registration, user)
                            hideProgressDialog(animation_lottie)

                        } else {
                            hideProgressDialog(animation_lottie)
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    }
                )
        }
    }

    fun backToHome(view: View) {
        startActivity(Intent(this, Authorization::class.java))
        finish()
    }


    fun userRegistrationSuccess() {

        showErrorSnackBar("Вы успешно зарегистрировались. Ваш id: ${firebaseUser.uid}", false)
        et_firstName.text = null
        et_surname.text = null
        email_registration.text = null
        password_registration.text = null

    }
    fun userRegistrationFailed() {
        showErrorSnackBar("Ошибка во врeмя ригистрации", true)
    }


    private fun validateRegister(): Boolean {
        return when {
            TextUtils.isEmpty(email_registration.text.toString().trim{ it <= ' ' }) -> {
                showErrorSnackBar("Вы забыли ввести email", true)
                false
            }
            TextUtils.isEmpty(password_registration.text.toString().trim{ it <= ' '}) -> {
                showErrorSnackBar("Вы забыли ввести пароль",true)
                false
            }
            TextUtils.isEmpty(et_firstName.text.toString().trim{ it <= ' '}) -> {
                showErrorSnackBar("Вы забыли ввести имя",true)
                false
            }
            TextUtils.isEmpty(et_surname.text.toString().trim{ it <= ' '}) -> {
                showErrorSnackBar("Вы забыли ввести фамилию",true)
                false
            }
            ' ' in et_firstName.text.toString().trim{ it <= ' '} -> {
                showErrorSnackBar("Некорректный ввод имени",true)
                false
            }
            ' ' in et_surname.text.toString().trim{ it <= ' '} -> {
                showErrorSnackBar("Некорректный ввод фамилии",true)
                false
            }

            '@' !in email_registration.text.toString().trim{ it <= ' ' } -> {
                showErrorSnackBar("Неверный формат email",true)
                false
            }
            else -> {
                true
            }
        }
    }

}