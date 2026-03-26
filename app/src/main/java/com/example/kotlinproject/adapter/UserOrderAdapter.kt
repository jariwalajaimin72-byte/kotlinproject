package com.example.kotlinproject.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlinproject.R
import com.example.kotlinproject.activity.OrderDetailsActivity
import com.example.kotlinproject.model.OrderModel

class UserOrderAdapter(
    private val context: Context,
    private val orderList: ArrayList<OrderModel>
) : RecyclerView.Adapter<UserOrderAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvProductName: TextView = itemView.findViewById(R.id.tvProductName)
        val tvTotal: TextView = itemView.findViewById(R.id.tvTotal)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val tvUserName: TextView? = itemView.findViewById(R.id.tvUserName) // optional
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_user_order, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = orderList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = orderList[position]

        // Product Name
        holder.tvProductName.text = order.productName ?: "No Name"

        // Total Amount
        holder.tvTotal.text = "Total: ₹${order.totalAmount ?: 0.0}"

        // Order Status
        holder.tvStatus.text = order.orderStatus ?: "Pending"

        // Optional User Name
        holder.tvUserName?.text = order.userName ?: ""

        // Product Image with Glide


        // Status Colors
        when (order.orderStatus) {
            "Pending" -> holder.tvStatus.setTextColor(Color.parseColor("#FFA500")) // Orange
            "Shipped" -> holder.tvStatus.setTextColor(Color.parseColor("#2196F3")) // Blue
            "Delivered" -> holder.tvStatus.setTextColor(Color.parseColor("#4CAF50")) // Green
            "Cancelled" -> holder.tvStatus.setTextColor(Color.parseColor("#F44336")) // Red
            else -> holder.tvStatus.setTextColor(Color.GRAY)
        }

        // Click to open Order Details


        holder.itemView.setOnClickListener {
            val intent = Intent(context, OrderDetailsActivity::class.java)
            intent.putExtra("orderId", order.orderId)
            context.startActivity(intent)
        }
    }

    // ✅ New method to update the adapter's data
    fun updateData(newList: ArrayList<OrderModel>) {
        orderList.clear()
        orderList.addAll(newList)
        notifyDataSetChanged()
    }
}