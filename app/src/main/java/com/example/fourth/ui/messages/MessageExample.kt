package com.example.fourth.ui.messages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fourth.R
import com.example.fourth.dataClasses.LastMessageInfo
import com.example.fourth.models.Constants.DATABASE_MESSAGES
import com.example.fourth.models.Constants.DATABASE_USERS
import com.example.fourth.models.FriendsUser
import com.example.fourth.models.MessageInfo
import com.example.fourth.ui.settings.FriendsActivity
import com.example.fourth.ui.tasks.support.CreateTask
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_message_example.*
import kotlinx.android.synthetic.main.view_messages.view.*
import java.text.SimpleDateFormat
import java.util.*

class MessageExample : AppCompatActivity(), View.OnClickListener {

    lateinit var getter: FriendsUser
    lateinit var currentId: String
    private lateinit var ref: DatabaseReference
    private lateinit var myMessagesAdapter: FirebaseRecyclerAdapter<MessageInfo, MessageHolder>
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_example)

        getter = intent.extras?.getParcelable<FriendsUser>("getter")!!
        currentId = FirebaseAuth.getInstance().currentUser!!.uid



        initRecyclerView()

        fillingToolbar()
        initListeners()

        newTask.setOnClickListener(this)

    }

    private fun getDateTime(): String {
        val format1 = SimpleDateFormat("yyyy-MM-dd")
        val format2 = SimpleDateFormat("HH:mm:ss")

        format1.timeZone = TimeZone.getTimeZone("GMT")
        format2.timeZone = TimeZone.getTimeZone("GMT")

        return "${format1.format(Date())} ${format2.format(Date())}"
    }


    private fun initListeners() {
        back_to_message.setOnClickListener(this)
        message.setEndIconOnClickListener {
            val text = message.editText?.text?.trim{ it <= ' '}.toString()
            if (text.isNotEmpty()) {
                recyclerView.smoothScrollToPosition(myMessagesAdapter.itemCount)
                val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())

                val userTime = getDateTime()
                Log.e("ADSFA", userTime)
                val databaseKey = "$date$time${System.currentTimeMillis()}"
                val infoSend = MessageInfo(
                        text,
                        true,
                        date,
                        time
                )
                val infoGet = MessageInfo(
                        text,
                        false,
                        date,
                        time
                )
                FirebaseFirestore.getInstance().collection("users").document(currentId)
                    .get().addOnSuccessListener {
                        val lastMessageSend = LastMessageInfo(
                            text,
                            date,
                            time.substringBeforeLast(":"),
                            getter.name!! + " " + getter.surname!!,
                            getter.image!!,
                            getter.id!!
                        )

                        val lastMessageGet = LastMessageInfo(
                            text,
                            date,
                            time.substringBeforeLast(":"),
                            it.get("name").toString() + " " + it.get("surname").toString(),
                            it.get("image").toString(),
                            it.get("id").toString()
                        )
                        Log.e("adscv", lastMessageGet.toString())
                        Firebase.database.reference.child("LAST").child(currentId).child(getter.id!!)
                            .setValue(lastMessageSend)
                        Firebase.database.reference.child("LAST").child(getter.id!!).child(currentId)
                            .setValue(lastMessageGet)
                    }
                Firebase.database.reference.child(DATABASE_MESSAGES).child(currentId).child(getter.id!!)
                        .child(databaseKey).setValue(infoSend)
                Firebase.database.reference.child(DATABASE_MESSAGES).child(getter.id!!).child(currentId)
                        .child(databaseKey).setValue(infoGet)
            }
            message.editText?.text?.clear()
        }
    }

    class MessageHolder(v: View): RecyclerView.ViewHolder(v) {
        val rightMessage = v.right_message
        val leftMessage = v.left_message
    }
    private fun initRecyclerView() {
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView_messages)
        ref = Firebase.database.reference.child(DATABASE_MESSAGES).child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child(getter.id!!)

        val options = FirebaseRecyclerOptions.Builder<MessageInfo>()
                .setQuery(ref, MessageInfo::class.java)
                .build()

        myMessagesAdapter = object : FirebaseRecyclerAdapter<MessageInfo, MessageHolder>(options) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.view_messages, parent, false)
                return MessageHolder(view)
            }

            override fun onBindViewHolder(holder: MessageHolder, position: Int, model: MessageInfo) {
                if (model.sender) {
                    holder.rightMessage.text = model.text
                    holder.leftMessage.visibility = View.INVISIBLE
                }
                else {
                    holder.leftMessage.text = model.text
                    holder.rightMessage.visibility = View.INVISIBLE
                }
            }

            override fun getItemCount(): Int {
                return super.getItemCount()
            }

        }

        recyclerView.adapter = myMessagesAdapter
        recyclerView.smoothScrollToPosition(myMessagesAdapter.itemCount)
        myMessagesAdapter.startListening()
    }

    private fun fillingToolbar() {
        name_message.text = getter.name
        surname_message.text = getter.surname
        Glide.with(this).load(getter.image).into(avatar_message)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.back_to_message -> onBackPressed()
            R.id.newTask -> {
                val intent = Intent(this, CreateTask::class.java)
                intent.putExtra("to", getter)
                startActivity(intent)
            }
        }
    }
}