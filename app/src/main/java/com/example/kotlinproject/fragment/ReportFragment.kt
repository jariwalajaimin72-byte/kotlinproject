package com.example.kotlinproject.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinproject.R
import com.example.kotlinproject.adapter.OrderAdapter
import com.example.kotlinproject.model.OrderModel
import com.google.firebase.database.*

class ReportFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var orderList: ArrayList<OrderModel>
    private lateinit var adapter: OrderAdapter
    private lateinit var dbRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_report, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewOrders)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        orderList = ArrayList()
        try {
            adapter = OrderAdapter(orderList)
            recyclerView.adapter = adapter

            dbRef = FirebaseDatabase.getInstance().getReference("Orders")

            loadOrders()
        }catch (e: Exception){}


        return view
    }

    private fun loadOrders() {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                orderList.clear()
                for (orderSnap in snapshot.children) {
                    val order = orderSnap.getValue(OrderModel::class.java)
                    if (order != null) {
                        orderList.add(order)
                    }
                }
                // Latest orders show first
                orderList.reverse()
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Optional: Handle errors
            }
        })
    }
}
