package com.example.kotlinproject.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinproject.R
import com.example.kotlinproject.model.OrderModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class OrderAdapter(private val orderList: ArrayList<OrderModel>) :
    RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    // Firebase reference for "Orders"
    private val dbOrders: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("Orders")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order_admin, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = orderList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = orderList[position]

        // ✅ Safely set order details
        holder.tvUser.text = "Customer: ${order.userName ?: "Unknown"}"
        holder.tvProduct.text = "Product: ${order.productName ?: "Unknown Product"}"
        holder.tvTotal.text = "Total: ₹${order.totalAmount ?: 0.0}"
        holder.tvPayment.text = "Payment: ${order.paymentMethod ?: "N/A"}"
        holder.tvDate.text = "Date: ${order.date ?: "-"}"

        // ✅ Safe delete functionality
        holder.btnDelete.setOnClickListener {
            val context = holder.itemView.context

            AlertDialog.Builder(context)
                .setTitle("Delete Order")
                .setMessage("Are you sure you want to delete this order?")
                .setPositiveButton("Delete") { dialog, _ ->
                    val orderId = order.orderId
                    if (orderId != null) {
                        dbOrders.child(orderId).removeValue()
                            .addOnSuccessListener {
                                val pos = holder.adapterPosition
                                if (pos != RecyclerView.NO_POSITION && pos < orderList.size) {
                                    orderList.removeAt(pos)
                                    notifyItemRemoved(pos)
                                } else {
                                    notifyDataSetChanged()
                                }
                                Toast.makeText(
                                    context,
                                    "Order deleted successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    context,
                                    "Failed to delete order",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    // ✅ ViewHolder mapping all XML IDs
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvUser: TextView = itemView.findViewById(R.id.tvUser)
        val tvProduct: TextView = itemView.findViewById(R.id.tvProduct)
        val tvTotal: TextView = itemView.findViewById(R.id.tvTotal)
        val tvPayment: TextView = itemView.findViewById(R.id.tvPayment)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
    }
}
