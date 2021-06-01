package com.example.fourth.ui.messages

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fourth.R
import com.example.fourth.dataClasses.LastMessageInfo
import com.example.fourth.models.ChatFriendsAdapter
import com.example.fourth.models.Constants
import com.example.fourth.models.FriendsUser
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_messages.*
import kotlinx.android.synthetic.main.fragment_messages.view.*
import kotlinx.android.synthetic.main.view_last_message.view.*
import kotlinx.android.synthetic.main.view_newchat.view.*

class MessagesFragment : Fragment(), View.OnClickListener {
    private var onlineUserId = ""
    private lateinit var myMessagesAdapter: FirebaseRecyclerAdapter<LastMessageInfo, AllMessageHolder>
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_messages, container, false)
        onlineUserId = FirebaseAuth.getInstance().currentUser!!.uid

        root.bt_create_new_dialog.setOnClickListener(this)
        allLastRecycler(root)





        return root
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.bt_create_new_dialog -> {
                animation_lottie.visibility = View.VISIBLE
                makeFriendsMap()
            }
        }
    }

    private fun makeFriendsMap() {
        val listFriends = mutableListOf<FriendsUser>()
        Firebase.database.reference.child(Constants.DATABASE_FRIENDS).child(onlineUserId).get()
                .addOnSuccessListener {
                    for (i in it.children) {
                        val friend = FriendsUser(i.child(Constants.DATABASE_ID).value.toString(),
                                i.child(Constants.FIRESTORE_NAME).value.toString(),
                                i.child(Constants.FIRESTORE_SURNAME).value.toString(),
                                i.child(Constants.FIRESTORE_IMAGE).value.toString())
                        listFriends.add(friend)
                    }
                    initRecyclerView(listFriends)
                }
    }
    fun initRecyclerView(list: MutableList<FriendsUser>) {
        val includeLayout = this.layoutInflater.inflate(R.layout.support_newchat_recycler, null)
        val dialog = MaterialAlertDialogBuilder(requireActivity())
        val usersView = includeLayout.findViewById<RecyclerView>(R.id.recyclerView_users_newChat)

        usersView.layoutManager = LinearLayoutManager(activity)

        dialog.setView(includeLayout)
        val notBuilder = dialog.show()

        usersView.adapter = ChatFriendsAdapter(list, requireActivity(), notBuilder)
        animation_lottie.visibility = View.INVISIBLE
    }

    fun allLastRecycler(root: View) {
        val ref = Firebase.database.reference.child("LAST").child(onlineUserId)
        recyclerView = root.recyclerView_chats

        val options = FirebaseRecyclerOptions.Builder<LastMessageInfo>()
            .setQuery(ref, LastMessageInfo::class.java)
            .build()

        myMessagesAdapter = object : FirebaseRecyclerAdapter<LastMessageInfo, AllMessageHolder>(options) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllMessageHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.view_last_message, parent, false)
                return AllMessageHolder(view)
            }

            override fun onBindViewHolder(holder: AllMessageHolder, position: Int, model: LastMessageInfo) {
                holder.name.text = model.with
                holder.message.text = model.text
                holder.time.text = model.time
                Glide.with(this@MessagesFragment).load(model.withImg).into(holder.img)
                holder.itemView.setOnClickListener {
                    FirebaseFirestore.getInstance().collection("users").document(model.id)
                        .get().addOnSuccessListener {
                            val person = FriendsUser(
                                it.id,
                                it.getString("name"),
                                it.getString("surname"),
                                it.getString("image")
                            )
                            val intent = Intent(activity, MessageExample::class.java)
                            intent.putExtra("getter", person)
                            activity!!.startActivity(intent)
                        } }
            }

            override fun getItemCount(): Int {
                return super.getItemCount()
            }

        }
        recyclerView.adapter = myMessagesAdapter
        recyclerView.smoothScrollToPosition(myMessagesAdapter.itemCount)
        myMessagesAdapter.startListening()
    }

    class AllMessageHolder(v: View): RecyclerView.ViewHolder(v) {
        val name = v.task_title
        val img = v.task_img
        val message = v.lastMessage_message
        val time = v.lastMessage_time
    }
}