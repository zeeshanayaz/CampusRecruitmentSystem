package com.zeeshan.campusrecruitmentsystem.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.zeeshan.campusrecruitmentsystem.R
import com.zeeshan.campusrecruitmentsystem.model.ChatMessage
import java.text.SimpleDateFormat
import java.util.*

class UserChatListAdapter
    (
    var context: Context, var userChatList: ArrayList<ChatMessage>
) : RecyclerView.Adapter<UserChatListAdapter.myViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        return when (viewType) {
            0 -> {
                val view = LayoutInflater.from(context).inflate(R.layout.chat_layout_sender, null, false)
                myViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(context).inflate(R.layout.chat_layout_receiver, null, false)
                myViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return userChatList.size
    }

    override fun onBindViewHolder(viewHolder: myViewHolder, position: Int) {
        viewHolder.bindUser(userChatList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return when (userChatList[position].senderId) {
            FirebaseAuth.getInstance().uid -> {
                0
            }
            else -> {
                1
            }
        }
    }


    inner class myViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        val chatMsg = view.findViewById<TextView>(R.id.chat_msg)
        val nameTitle = view.findViewById<TextView>(R.id.msgOwnerTitle)
        val msgTime = view.findViewById<TextView>(R.id.msgTime)


        fun bindUser(msg: ChatMessage) {
            chatMsg.text = msg.msg

            val date = Date(msg.timestamp!!)
            val format = SimpleDateFormat("MMM d, yyyy h:mm a")   //"yyyy.MM.dd HH:mm"
            val time = format.format(date)

            msgTime.text = time.toString()

            Log.v("Date", time.toString())
        }
    }
}