package com.example.kotlinproject.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinproject.R
import com.example.kotlinproject.adapter.UserOrderAdapter
import com.example.kotlinproject.model.OrderModel
import com.google.firebase.database.*

class UserOrderActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tvNoOrder: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var orderList: ArrayList<OrderModel>
    private lateinit var adapter: UserOrderAdapter
    private lateinit var database: DatabaseReference
    private var orderListener: ValueEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_order)

        supportActionBar?.title = "My Orders"

        initViews()
        loadOrders()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.orderRecyclerView)
        tvNoOrder = findViewById(R.id.tvNoOrder)
        progressBar = findViewById(R.id.progressBar)

        recyclerView.layoutManager = LinearLayoutManager(this)

        orderList = ArrayList()
        adapter = UserOrderAdapter(this, orderList)
        recyclerView.adapter = adapter

        database = FirebaseDatabase.getInstance().getReference("Orders")
    }

    private fun loadOrders() {

        val currentUserId = getSharedPreferences("user_session", MODE_PRIVATE)
            .getString("user_id", "")

        if (currentUserId.isNullOrEmpty()) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show()
            tvNoOrder.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            progressBar.visibility = View.GONE
            return
        }

        progressBar.visibility = View.VISIBLE
        tvNoOrder.visibility = View.GONE
        recyclerView.visibility = View.GONE

        orderListener = database
            .orderByChild("userId")
            .equalTo(currentUserId)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    orderList.clear()

                    for (data in snapshot.children) {
                        val order = data.getValue(OrderModel::class.java)
                        if (order != null) {
                            orderList.add(order)
                        } else {
                            Log.w("UserOrderActivity", "Order null for key: ${data.key}")
                        }
                    }

                    // Reverse list to show latest orders first
                    orderList.reverse()

                    progressBar.visibility = View.GONE

                    if (orderList.isNotEmpty()) {
                        recyclerView.visibility = View.VISIBLE
                        tvNoOrder.visibility = View.GONE
                    } else {
                        recyclerView.visibility = View.GONE
                        tvNoOrder.visibility = View.VISIBLE
                    }

                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(
                        this@UserOrderActivity,
                        "Failed to load orders: ${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.e("UserOrderActivity", "Firebase error", error.toException())
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        orderListener?.let {
            database.removeEventListener(it)
        }
    }
}