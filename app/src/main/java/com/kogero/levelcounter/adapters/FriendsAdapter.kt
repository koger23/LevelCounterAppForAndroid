package com.kogero.levelcounter.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.kogero.levelcounter.R
import com.kogero.levelcounter.models.UserListViewModel
import kotlinx.android.synthetic.main.friend_list_item.view.*


class FriendsAdapter(
    private val context: Context,
    private val userList: ArrayList<UserListViewModel>,
    private val userFullList: ArrayList<UserListViewModel> = ArrayList()
) :
    RecyclerView.Adapter<FriendsAdapter.FriendViewHolder>() {

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        holder.friendName.text = userList[position].userName
        if (userFullList.size == 0) {
            userFullList.addAll(userList)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        return FriendViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.friend_list_item, parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun filterUsers(query: String): List<UserListViewModel> {
        val query = query.toLowerCase()
        userList.clear()
        for (user1 in userFullList) {
            if (user1.userName.toLowerCase().contains(query)) {
                userList.add(user1)
            }
        }
        if (userList.isEmpty()) {
            Toast.makeText(this.context, "No match", Toast.LENGTH_SHORT).show()
        }
        notifyDataSetChanged()
        return userList
    }

    inner class FriendViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val friendName: TextView = view.friendName

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View) {
        }

    }
}
