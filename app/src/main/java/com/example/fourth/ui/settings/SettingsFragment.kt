package com.example.fourth.ui.settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.fourth.R
import com.example.fourth.activities.Authorization
import com.example.fourth.models.Constants
import com.example.fourth.models.Constants.FIRESTORE_BIRTH
import com.example.fourth.models.Constants.FIRESTORE_EMAIL
import com.example.fourth.models.Constants.FIRESTORE_IMAGE
import com.example.fourth.models.Constants.FIRESTORE_NAME
import com.example.fourth.models.Constants.FIRESTORE_PASSWORD
import com.example.fourth.models.Constants.FIRESTORE_PHONE
import com.example.fourth.models.Constants.FIRESTORE_SURNAME
import com.example.fourth.models.LoggedUserInfo
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_settings.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class SettingsFragment : Fragment(), View.OnClickListener {
    var myRef: SharedPreferences? = null
    var userId: String? = null
    private var user: LoggedUserInfo ?= null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_settings, container, false)

        root.bt_signOut.setOnClickListener(this)
        root.bt_editProfile.setOnClickListener(this)
        root.bt_settings_support.setOnClickListener(this)
        root.bt_friends.setOnClickListener(this)
        root.changePassword.setOnClickListener(this)

        root.tv_nameSurname.movementMethod = ScrollingMovementMethod()    //scroll the text view with full name

        myRef = activity?.getSharedPreferences(Constants.KEY, Context.MODE_PRIVATE)
        userId = FirebaseAuth.getInstance().currentUser!!.uid

        GlobalScope.launch {
            FirebaseFirestore.getInstance().collection("users").document(userId!!).get().addOnCompleteListener {
                if (it.isSuccessful) {
                    user = LoggedUserInfo(
                        userId,
                        it.result?.getString(FIRESTORE_NAME),
                        it.result?.getString(FIRESTORE_SURNAME),
                        it.result?.getString(FIRESTORE_EMAIL),
                        it.result?.getString(FIRESTORE_IMAGE),
                        it.result?.getString(FIRESTORE_PASSWORD),
                        it.result?.getString(FIRESTORE_BIRTH),
                        it.result?.getString(FIRESTORE_PHONE)
                    )
                    root.tv_nameSurname.text = user?.name + " " + user?.surname
                    Glide.with(requireActivity()).load(user?.image).into(root.iv_settings_avatar)
                    root.animation_lottie.visibility = View.INVISIBLE
                }
                else {
                    Toast.makeText(context, "Error, check Internet connection and restart me", 3000L.toInt()).show()
                }
            }
        }


        return root
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bt_signOut -> {
                myRef?.edit()?.clear()?.apply()
                val intent = Intent(activity, Authorization::class.java)
                startActivity(intent)
                activity?.finish()
            }
            R.id.bt_editProfile -> {
                val intent = Intent(activity, EditProfile::class.java)
                intent.putExtra("user", user)
                startActivity(intent)
            }
            R.id.bt_settings_not -> {}
            R.id.bt_settings_support -> supportDialog()
            R.id.bt_friends -> {
                startActivity(Intent(activity, FriendsActivity::class.java))
            }
            R.id.changePassword -> {
                changePassword()
            }
        }
    }

    private fun changePassword() {
        val includeLayout = this.layoutInflater.inflate(R.layout.support_dialog, null)
        val dialog = MaterialAlertDialogBuilder(requireActivity())
        dialog.setView(includeLayout)

        val textLayout = includeLayout.findViewById<TextInputLayout>(R.id.support_message)
        textLayout.hint = "Enter your email"
        val editText = textLayout.editText?.text


        dialog.setNegativeButton(resources.getString(R.string.cancel)) { f, _ ->
            f.cancel()
        }
        dialog.setPositiveButton(resources.getString(R.string.send)) { f, _ ->
            FirebaseAuth.getInstance().sendPasswordResetEmail(editText.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) Toast.makeText(
                        requireContext(),
                        "Check this email to reset passwort",
                        Toast.LENGTH_LONG
                    ).show()
                    else Toast.makeText(
                        requireContext(),
                        "Something went wrong(",
                        Toast.LENGTH_LONG
                    ).show()
                }
            f.cancel()

        }.show()
    }

    fun supportDialog() {
        val includeLayout = this.layoutInflater.inflate(R.layout.support_dialog, null)
        val dialog = MaterialAlertDialogBuilder(requireActivity())
        dialog.setView(includeLayout)
        val editText = includeLayout.findViewById<TextInputLayout>(R.id.support_message).editText?.text
        dialog.setNegativeButton(resources.getString(R.string.cancel)) {
            f, _ -> f.cancel()
        }
        dialog.setPositiveButton(resources.getString(R.string.send)) {
            f, _ ->

            val to = "nigmatullin1441@gmail.com"
            val title = "Support"

            val email = Intent(Intent.ACTION_SEND)

            email.putExtra(Intent.EXTRA_EMAIL, to)
            email.putExtra(Intent.EXTRA_SUBJECT, title)
            email.putExtra(Intent.EXTRA_TEXT, editText.toString())
            email.setType("message/rfc822")
            startActivity(Intent.createChooser(email, "Выберите email клиент :"))

            Toast.makeText(context, "Thanks for message", Toast.LENGTH_LONG).show()
            f.cancel()
        }.show()
    }
}