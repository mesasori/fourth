package com.example.fourth.ui.messages

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fourth.R
import com.example.fourth.models.Constants

class MessagesFragment : Fragment() {

    var myRef: SharedPreferences? = null
    var userId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_messages, container, false)
        myRef = activity?.getSharedPreferences(Constants.KEY, Context.MODE_PRIVATE)
        userId = myRef?.getString(Constants.USER_ID, "none")




        return root
    }
}