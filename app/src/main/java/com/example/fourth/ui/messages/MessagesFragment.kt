package com.example.fourth.ui.messages

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fourth.R
import com.example.fourth.models.Constants
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_messages.*
import kotlinx.android.synthetic.main.fragment_messages.view.*

class MessagesFragment : Fragment(), View.OnClickListener {

    var myRef: SharedPreferences? = null
    var userId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_messages, container, false)
        myRef = activity?.getSharedPreferences(Constants.KEY, Context.MODE_PRIVATE)
        userId = FirebaseAuth.getInstance().currentUser!!.uid

        root.bt_create_new_dialog.setOnClickListener(this)




        return root
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.bt_create_new_dialog -> {
                Snackbar.make(requireView(), "Pressed create new dialog button $userId", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show()
            }
        }
    }
}