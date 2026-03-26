package com.example.kotlinproject.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinproject.R
import com.example.kotlinproject.model.UserModel
import android.widget.TextView

class UserAdapter(private val userList: ArrayList<UserModel>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txtUserAvatar: TextView = itemView.findViewById(R.id.txtUserAvatar)
        val txtUserName: TextView = itemView.findViewById(R.id.txtUserName)
        val txtUserEmail: TextView = itemView.findViewById(R.id.txtUserEmail)
        val txtUserPassword: TextView = itemView.findViewById(R.id.txtUserPassword)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_user, parent, false)

        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        val user = userList[position]

        // Name
        holder.txtUserName.text = user.name

        // Email
        holder.txtUserEmail.text = user.email

        // 🔴 Never show real password
        holder.txtUserPassword.text = "Password: ••••••••"

        // Avatar First Letter
        if (user.name.isNotEmpty()) {
            holder.txtUserAvatar.text = user.name.substring(0,1).uppercase()
        } else {
            holder.txtUserAvatar.text = "U"
        }
    }
}