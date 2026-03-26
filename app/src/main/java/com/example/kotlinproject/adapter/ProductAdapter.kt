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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.FirebaseDatabase

class ProductAdapter(
    private val productList: ArrayList<ProductModel>
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProd: ImageView = itemView.findViewById(R.id.imgProd)
        val tvProdName: TextView = itemView.findViewById(R.id.tvProdName)
        val tvProdPrice: TextView = itemView.findViewById(R.id.tvProdPrice)
        val btnEdit: Button = itemView.findViewById(R.id.btnProdEdit)
        val btnDelete: Button = itemView.findViewById(R.id.btnProdDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]

        holder.tvProdName.text = product.pname ?: "N/A"
        holder.tvProdPrice.text = "₹ ${product.price ?: "0"}"

        // Decode Base64 image safely
        val bitmap = product.pic?.takeIf { it.isNotEmpty() }?.let {
            try {
                val bytes = Base64.decode(it, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        if (bitmap != null) holder.imgProd.setImageBitmap(bitmap)
        else holder.imgProd.setImageResource(R.mipmap.logo_one)

        // Edit click
        holder.btnEdit.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Edit not implemented", Toast.LENGTH_SHORT).show()
        }

        // Delete click
        holder.btnDelete.setOnClickListener {
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(holder.itemView.context)
            materialAlertDialogBuilder.setTitle("Permanent Delete")
            materialAlertDialogBuilder.setMessage("Are you sure want to delete?")
            materialAlertDialogBuilder.setPositiveButton("Yes") { _, _ ->
                val pid = product.pid
                if (pid != null) {
                    val firebaseDatabase = FirebaseDatabase.getInstance()
                    val productRef = firebaseDatabase.getReference("product")
                    productRef.child(pid).removeValue()
                        .addOnSuccessListener {
                            Toast.makeText(holder.itemView.context, "Deleted Successfully", Toast.LENGTH_LONG).show()
                            productList.removeAt(position)
                            notifyItemRemoved(position)
                            notifyItemRangeChanged(position, productList.size)
                        }
                        .addOnFailureListener {
                            Toast.makeText(holder.itemView.context, "Not deleted", Toast.LENGTH_LONG).show()
                        }
                } else {
                    Toast.makeText(holder.itemView.context, "Invalid Product ID", Toast.LENGTH_SHORT).show()
                }
            }

            materialAlertDialogBuilder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }

            materialAlertDialogBuilder.show()
        }
    }

    override fun getItemCount(): Int = productList.size
}
