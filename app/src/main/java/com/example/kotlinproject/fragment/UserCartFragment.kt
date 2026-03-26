package com.example.kotlinproject.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinproject.R
import com.example.kotlinproject.activity.PaymentMethodActivity
import com.example.kotlinproject.adapter.CartAdapter
import com.example.kotlinproject.model.ProductModel
import com.google.firebase.database.*

class UserCartFragment : Fragment() {

    private lateinit var recyclerCart: RecyclerView
    private lateinit var tvTotal: TextView
    private lateinit var btnProceedPayment: Button
    private lateinit var adapter: CartAdapter
    private val cartList = ArrayList<ProductModel>()
    private lateinit var cartRef: DatabaseReference
    var pname: String=""
    var pid: String=""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_user_cart, container, false)

        recyclerCart = view.findViewById(R.id.recyclerCart)
        tvTotal = view.findViewById(R.id.tvTotal)
        btnProceedPayment = view.findViewById(R.id.btnProceedPayment)

        recyclerCart.layoutManager = LinearLayoutManager(requireContext())
        adapter = CartAdapter(cartList) { updateTotalAmount() }
        recyclerCart.adapter = adapter

        cartRef = FirebaseDatabase.getInstance().getReference("cart")
        fetchCartItems()

        btnProceedPayment.setOnClickListener {
            val totalText = tvTotal.text.toString().replace("Total: ₹", "").trim()
            val totalAmount = totalText.toDoubleOrNull() ?: 0.0

            if (totalAmount <= 0) {
                Toast.makeText(requireContext(), "Your cart is empty!", Toast.LENGTH_SHORT).show()
            } else {
                // ✅ Correct and safe Intent call
                val intent = Intent(requireActivity(), PaymentMethodActivity::class.java)
                intent.putExtra("TOTAL_AMOUNT", totalAmount)
                intent.putExtra("PRODUCT_NAME", pname)
                intent.putExtra("PRODUCT_ID", id)
                startActivity(intent)
            }
        }

        return view
    }

    private fun fetchCartItems() {
        cartRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!isAdded) return

                cartList.clear()
                for (item in snapshot.children) {
                    val product = item.getValue(ProductModel::class.java)
                    if (product != null) {
                        if (product.quantity <= 0) product.quantity = 1
                        pname=product.pname + " "
                        pid=product.pid +""
                        cartList.add(product)
                    }
                }
                adapter.notifyDataSetChanged()
                updateTotalAmount()
            }

            override fun onCancelled(error: DatabaseError) {
                if (!isAdded) return
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateTotalAmount() {
        var total = 0.0
        for (product in cartList) {
            val cleanPrice = product.price?.replace("₹", "")?.replace(",", "")?.trim() ?: "0"
            val price = cleanPrice.toDoubleOrNull() ?: 0.0
            val qty = if (product.quantity > 0) product.quantity else 1
            total += price * qty
        }
        tvTotal.text = "Total: ₹%.2f".format(total)
    }
}
