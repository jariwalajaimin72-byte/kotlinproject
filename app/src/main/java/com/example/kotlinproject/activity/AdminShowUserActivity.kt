package com.example.kotlinproject.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinproject.R
import com.example.kotlinproject.adapter.UserAdapter
import com.example.kotlinproject.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminShowUserActivity : AppCompatActivity() {
    private lateinit var rcylUser: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_show_user)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        rcylUser=findViewById(R.id.rcylUser)
        loadUser()
        recyclerView = findViewById(R.id.rcylUser)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        userList = ArrayList()
        userAdapter = UserAdapter(userList)
        recyclerView.adapter = userAdapter
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var userList: ArrayList<UserModel>
    private lateinit var userAdapter: UserAdapter
    fun loadUser(){
        val ref = FirebaseDatabase.getInstance().getReference("Users")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                userList.clear()

                for (userSnap in snapshot.children) {
                    val user = userSnap.getValue(UserModel::class.java)
                    if (user != null) {
                        userList.add(user)
                    }
                }

                userAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}