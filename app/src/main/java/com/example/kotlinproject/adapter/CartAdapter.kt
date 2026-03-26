package com.example.kotlinproject.adapter

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinproject.R
import com.example.kotlinproject.model.ProductModel
import com.google.firebase.database.FirebaseDatabase

class CartAdapter(
    private val cartList: MutableList<ProductModel>,
    private val onCartUpdated: () -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProd: ImageView = itemView.findViewById(R.id.imgCartProduct)
        val tvName: TextView = itemView.findViewById(R.id.tvCartName)
        val tvPrice: TextView = itemView.findViewById(R.id.tvCartPrice)
        val btnRemove: Button = itemView.findViewById(R.id.btnRemoveCart)
        val btnMinus: Button = itemView.findViewById(R.id.btnMinus)
        val btnPlus: Button = itemView.findViewById(R.id.btnPlus)
        val tvQty: TextView = itemView.findViewById(R.id.tvQty)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_cart_product, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val product = cartList[position]

        holder.tvName.text = product.pname ?: "N/A"
        holder.tvPrice.text = "₹${product.price ?: "0"}"
        holder.tvQty.text = product.quantity.toString()

        // ✅ Load Image (no crash)
        loadProductImage(holder.imgProd, product.pic)

        val cartRef = FirebaseDatabase.getInstance().getReference("cart")

        // 🔻 Minus
        holder.btnMinus.setOnClickListener {
            if (product.quantity > 1) {
                product.quantity--

                cartRef.child(product.pid ?: return@setOnClickListener)
                    .child("quantity")
                    .setValue(product.quantity)
                    .addOnSuccessListener {
                        notifyItemChanged(position)
                        onCartUpdated()
                    }
            } else {
                Toast.makeText(holder.itemView.context, "Minimum quantity is 1", Toast.LENGTH_SHORT).show()
            }
        }

        // 🔺 Plus
        holder.btnPlus.setOnClickListener {
            product.quantity++

            cartRef.child(product.pid ?: return@setOnClickListener)
                .child("quantity")
                .setValue(product.quantity)
                .addOnSuccessListener {
                    notifyItemChanged(position)
                    onCartUpdated()
                }
        }

        // ❌ Remove
        holder.btnRemove.setOnClickListener {
            val context = holder.itemView.context
            val productId = product.pid ?: return@setOnClickListener

            cartRef.child(productId)
                .removeValue()
                .addOnSuccessListener {
                    Toast.makeText(context, "Removed from Cart", Toast.LENGTH_SHORT).show()

                    val currentPosition = holder.adapterPosition
                    if (currentPosition != RecyclerView.NO_POSITION) {
                        cartList.removeAt(currentPosition)
                        notifyItemRemoved(currentPosition)
                        notifyItemRangeChanged(currentPosition, cartList.size)
                        onCartUpdated()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    // ✅ Image Loader (ImageView version)
    private fun loadProductImage(imageView: ImageView, base64Image: String?) {
        if (!base64Image.isNullOrEmpty()) {
            try {
                val decodedBytes = Base64.decode(base64Image, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap)
                } else {
                    imageView.setImageResource(R.mipmap.logo_one)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                imageView.setImageResource(R.mipmap.logo_one)
            }
        } else {
            imageView.setImageResource(R.mipmap.logo_one)
        }
    }

    override fun getItemCount(): Int = cartList.size
}