package com.example.fourth.ui.settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.solver.widgets.ConstraintWidget.VISIBLE
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.fourth.BaseActivity
import com.example.fourth.R
import com.example.fourth.activities.Authorization
import com.example.fourth.models.Constants
import com.example.fourth.models.LoggedUserInfo
import com.example.fourth.models.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings.view.*


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
        root.tv_nameSurname.movementMethod = ScrollingMovementMethod()    //scroll the text view with full name

        myRef = activity?.getSharedPreferences(Constants.KEY, Context.MODE_PRIVATE)
        userId = myRef?.getString(Constants.USER_ID, "none")

        root.lottie_constraint.visibility = View.VISIBLE
        FirebaseFirestore.getInstance().collection("users").document(userId!!).get().addOnCompleteListener {
            if (it.isSuccessful) {
                user = LoggedUserInfo(
                        userId,
                        it.result?.getString("firstName"),
                        it.result?.getString("surname"),
                        it.result?.getString("email"),
                        it.result?.getString("password"),
                        it.result?.getString("birth"),
                        it.result?.getString("phone")
                )
                root.tv_nameSurname.text = user?.name + " " + user?.surname
            }
            else {
                Toast.makeText(context, "Error, check Internet connection and restart me", 3000L.toInt()).show()
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
        }
    }
}