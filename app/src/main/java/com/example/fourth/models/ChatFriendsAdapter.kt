package com.example.fourth.models

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fourth.R
import com.example.fourth.ui.messages.MessageExample
import kotlinx.android.synthetic.main.view_newchat.view.*

class ChatFriendsAdapter(
        private val friendsList: MutableList<FriendsUser>,
        private val activity: Activity,
        private val dialog: androidx.appcompat.app.AlertDialog
) : RecyclerView.Adapter<ChatFriendsAdapter.FriendsViewHolder>() {

    class FriendsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val friendImage = itemView.iv_friend_chat
        val friendName = itemView.tv_userChat_name
        val friendSurname = itemView.tv_userChat_surname
        val messageButton = itemView.bt_start_chat
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.view_newchat, parent, false)
        return FriendsViewHolder(item)
    }

    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
        val currentItem = friendsList[position]
        Glide.with(activity).load(currentItem.image).into(holder.friendImage)
        holder.friendName.text = currentItem.name
        holder.friendSurname.text = currentItem.surname
        holder.itemView.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(activity, MessageExample::class.java)
            intent.putExtra("getter", currentItem)
            activity.startActivity(intent)
        }
        holder.messageButton.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(activity, MessageExample::class.java)
            intent.putExtra("getter", currentItem)
            activity.startActivity(intent)
        }

    }

    override fun getItemCount() = friendsList.size
}