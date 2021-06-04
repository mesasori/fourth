package com.example.fourth.ui.tasks.support

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fourth.R
import com.example.fourth.models.FriendsUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_create_task.*
import java.text.SimpleDateFormat
import java.util.*

class CreateTask : AppCompatActivity() {
    lateinit var getter: FriendsUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_task)
        val currentId = FirebaseAuth.getInstance().currentUser!!.uid

        getter = intent.getParcelableExtra("to")!!
        sendTask.setOnClickListener {
            val time = SimpleDateFormat("HH:mm:ss")
            time.timeZone = TimeZone.getTimeZone("GMT")
            val task = TaskInfo(
                getter.id!!,
                currentId,
                time.toString(),
                titleTask.editText!!.text.toString(),
                task_image.toString(),
                task_description.editText!!.text.toString()
            )

            FirebaseDatabase.getInstance().reference.child("tasks").child(currentId).child(getter.id!!)
                .setValue(task)
            FirebaseDatabase.getInstance().reference.child("tasks").child(getter.id!!).child(currentId)
                .setValue(task)

            onBackPressed()
        }
    }
}