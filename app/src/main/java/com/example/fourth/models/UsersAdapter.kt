package com.example.fourth.models

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fourth.R
import com.example.fourth.models.Constants.DATABASE_FRIENDS
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.users_recycler_view.view.*

class UsersAdapter(
        private val usersList: MutableList<FriendsUser>,
        private val activity: Activity, private val currentUser: FriendsUser
        ): RecyclerView.Adapter<UsersAdapter.UsersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.users_recycler_view, parent, false)
        return UsersViewHolder(item)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        var state = false
        val currentItem = usersList[position]
        val profileId = FirebaseAuth.getInstance().currentUser!!.uid

        Glide.with(activity).load(currentItem.image).into(holder.userImage!!)
        holder.userName?.text = currentItem.name
        holder.userSurname?.text = currentItem.surname
        holder.addButton?.setOnClickListener{
            if (!state) {
                Firebase.database.reference.child(DATABASE_FRIENDS)
                    .child(profileId).child(currentItem.id).setValue(currentItem)
                Firebase.database.reference.child(DATABASE_FRIENDS)
                    .child(currentItem.id).child(profileId).setValue(currentUser)
                holder.addButton.setIconResource(R.drawable.ic_baseline_check_24)
                state = true
            }
            else {
                Firebase.database.reference.child(DATABASE_FRIENDS)
                    .child(profileId).child(currentItem.id).removeValue()
                Firebase.database.reference.child(DATABASE_FRIENDS)
                    .child(currentItem.id).child(profileId).removeValue()


                holder.addButton.setIconResource(R.drawable.ic_baseline_person_add_24)
                state = false
            }
        }
    }

    override fun getItemCount() = usersList.size

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    class UsersViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val userImage: ImageView ?= itemView.iv_user_avatar
        val userName: TextView ?= itemView.tv_user_name
        val userSurname: TextView ?= itemView.tv_user_surname
        val addButton: MaterialButton?= itemView.bt_user_add
    }
}