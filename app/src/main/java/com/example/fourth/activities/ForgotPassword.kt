package com.example.fourth.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.fourth.BaseActivity
import com.example.fourth.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPassword : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        moveToAuthorization.setOnClickListener {
            val intent = Intent(this, Authorization::class.java)
            startActivity(intent)
        }

        bt_submit.setOnClickListener {
            showProgressDialog(animation_lottie)
            val email = et_restorePassword.text.toString().trim{ it <= ' '}

            if (email.isEmpty()) {
                showErrorSnackBar("Поле email пустое", false)
            }
            else {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if(task.isSuccessful) {
                            hideProgressDialog(animation_lottie)
                            Toast.makeText(this, "Код восстановления был успешно отправлен", Toast.LENGTH_LONG)
                                .show()
                            finish()
                        } else {
                            hideProgressDialog(animation_lottie)
                            showErrorSnackBar(task!!.exception.toString(), true)
                        }
                    }
            }
        }
    }
}