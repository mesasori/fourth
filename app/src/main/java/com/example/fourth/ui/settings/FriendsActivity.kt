package com.example.fourth.ui.settings

import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fourth.R
import com.example.fourth.models.Constants
import com.example.fourth.models.Constants.DATABASE_ID
import com.example.fourth.models.Constants.FIRESTORE_EMAIL
import com.example.fourth.models.Constants.FIRESTORE_IMAGE
import com.example.fourth.models.Constants.FIRESTORE_NAME
import com.example.fourth.models.Constants.FIRESTORE_SURNAME
import com.example.fourth.models.FriendsUser
import com.example.fourth.models.UsersAdapter
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_friends.*
import kotlinx.android.synthetic.main.fragment_settings.view.*
import kotlinx.android.synthetic.main.friends_recycler_view.view.*
import kotlinx.android.synthetic.main.support_users_recycler.*

class FriendsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var myFriendsAdapter: FirebaseRecyclerAdapter<FriendsUser, FriendsHolder>
    private lateinit var refFriends: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)

        initButtonListeners()
        initRecyclerView()

    }

    fun initButtonListeners() {
        bt_backToSettings_fromFriend.setOnClickListener(this)
        bt_findFriend.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.bt_backToSettings_fromFriend -> onBackPressed()
            R.id.bt_findFriend -> {
                makeUsersMap()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        myFriendsAdapter.stopListening()
    }
    class FriendsHolder(v: View): RecyclerView.ViewHolder(v) {
        val name = v.tv_friends_name
        val surname = v.tv_friends_surname
        val image = v.iv_avatar
        val message = v.bt_friends_send
        val remove = v.bt_friends_remove
    }
    fun initRecyclerView()  {
        animation_lottie_friends.visibility = View.VISIBLE
        recyclerView = findViewById(R.id.recyclerView)
        refFriends = Firebase.database.reference.child(Constants.DATABASE_FRIENDS).child(FirebaseAuth.getInstance().currentUser!!.uid)

        val options = FirebaseRecyclerOptions.Builder<FriendsUser>()
                .setQuery(refFriends, FriendsUser::class.java)
                .build()

        myFriendsAdapter = object :FirebaseRecyclerAdapter<FriendsUser, FriendsHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.friends_recycler_view, parent, false)
                animation_lottie_friends.visibility = View.INVISIBLE
                return FriendsHolder(view)
            }

            override fun onBindViewHolder(holder: FriendsHolder, position: Int, model: FriendsUser) {
                if (model.surname!!.isEmpty()) {
                    holder.surname.text = model.name
                    holder.name.text = "Of your app"
                }
                else {
                    holder.surname.text = model.surname
                    holder.name.text = model.name
                }
                Glide.with(this@FriendsActivity).load(model.image).into(holder.image)
                holder.message.setOnClickListener {
                    supportDialog(model.name!!)
                }
                holder.remove.setOnClickListener {
                    if (model.id != "JShxl9yhuNgguhfWx0IqmRq390K2") {
                        Firebase.database.reference.child(Constants.DATABASE_FRIENDS).child(FirebaseAuth.getInstance().currentUser!!.uid).child(model.id!!).removeValue()
                        Firebase.database.reference.child(Constants.DATABASE_FRIENDS).child(model.id!!).child(FirebaseAuth.getInstance().currentUser!!.uid).removeValue()
                    }
                    else Toast.makeText(this@FriendsActivity, "You can't remove administrator from friends", Toast.LENGTH_LONG).show()
                }
            }

        }

        recyclerView.adapter = myFriendsAdapter
        myFriendsAdapter.startListening()

    }

    private fun supportDialog(userId: String) {
        val includeLayout = this.layoutInflater.inflate(R.layout.support_dialog, null)
        val dialog = MaterialAlertDialogBuilder(this)
        val textInput = includeLayout.findViewById<TextInputLayout>(R.id.support_message)
        val editText = textInput.editText?.text
        textInput.hint = "Message to $userId"

        dialog.setView(includeLayout)

        dialog.setNegativeButton(resources.getString(R.string.cancel)) {
            f, _ -> f.cancel()
        }

        dialog.setPositiveButton(resources.getString(R.string.send)) {
            f, _ ->
            Toast.makeText(this, editText.toString(), Toast.LENGTH_LONG).show()
            f.cancel()
        }.show()
    }


    private fun inflateUsers(list: MutableList<FriendsUser>, mainUser: FriendsUser) {
        val includeLayout = this.layoutInflater.inflate(R.layout.support_users_recycler, null)
        val dialog = MaterialAlertDialogBuilder(this)
        val usersView = includeLayout.findViewById<RecyclerView>(R.id.recyclerView_users)

        usersView.layoutManager = LinearLayoutManager(this)
        usersView.adapter = UsersAdapter(list, this, mainUser)

        dialog.setView(includeLayout).show()
    }
    private fun makeUsersMap() {
        val mapFriends = mutableMapOf<String, FriendsUser>()
        Firebase.database.reference.child(Constants.DATABASE_FRIENDS).child(FirebaseAuth.getInstance().currentUser!!.uid).get()
            .addOnSuccessListener {
                for (i in it.children) {
                    val friend = FriendsUser(i.child(DATABASE_ID).value.toString(),
                        i.child(FIRESTORE_NAME).value.toString(),
                        i.child(FIRESTORE_SURNAME).value.toString(),
                        i.child(FIRESTORE_IMAGE).value.toString())
                    mapFriends[friend.id!!] = friend
                }
                getUsersList(mapFriends)
            }
    }

    private fun getUsersList(mapFriends: MutableMap<String, FriendsUser>) {
        val list = mutableListOf<FriendsUser>()
        var mainUser = FriendsUser()
        Firebase.database.reference.child(Constants.DATABASE_USERS).get()
                .addOnSuccessListener {
                    for (i in it.children) {
                       if (i.child(DATABASE_ID).value.toString() != FirebaseAuth.getInstance().currentUser?.uid
                           && mapFriends[i.child(DATABASE_ID).value.toString()] == null)
                            {
                            list.add(FriendsUser(i.child(DATABASE_ID).value.toString(),
                                    i.child(FIRESTORE_NAME).value.toString(),
                                    i.child(FIRESTORE_SURNAME).value.toString(),
                                    i.child(FIRESTORE_IMAGE).value.toString()))
                       }
                        if (i.child(DATABASE_ID).value.toString() == FirebaseAuth.getInstance().currentUser?.uid)
                            mainUser = FriendsUser(i.child(DATABASE_ID).value.toString(),
                                i.child(FIRESTORE_NAME).value.toString(),
                                i.child(FIRESTORE_SURNAME).value.toString(),
                                i.child(FIRESTORE_IMAGE).value.toString())
                    }
                    inflateUsers(list, mainUser)
                }
    }
}