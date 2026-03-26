package com.example.kotlinproject.adapter

import android.graphics.Bitmap
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
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.ViewHolder

class ProductUserAdapter(
    private var productList: ArrayList<ProductModel>
) : RecyclerView.Adapter<ProductUserAdapter.ProductViewHolder>() {

    private var originalList: ArrayList<ProductModel> = ArrayList(productList)

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProd: ImageView = itemView.findViewById(R.id.imgProds)
        val tvPname: TextView = itemView.findViewById(R.id.tvPnames)
        val tvPrice: TextView = itemView.findViewById(R.id.tvPrices)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_user_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]

        holder.tvPname.text = product.pname ?: "N/A"
        holder.tvPrice.text = "₹ ${product.price ?: "0"}"

        val bitmap = product.pic?.let { base64ToBitmap(it) }
        if (bitmap != null) holder.imgProd.setImageBitmap(bitmap)
        else holder.imgProd.setImageResource(R.mipmap.logo_one)

        holder.itemView.setOnClickListener {
            val dialog = DialogPlus.newDialog(holder.imgProd.context)
                .setExpanded(true, 1700)
                .setContentHolder(ViewHolder(R.layout.activity_product_detail))
                .create()
            dialog.show()

            val view: View? = dialog.holderView
            view?.let {
                val imgDetail: ImageView = it.findViewById(R.id.imgProd)
                val tvName: TextView = it.findViewById(R.id.tvpname)
                val tvDesc: TextView = it.findViewById(R.id.tvpdesc)
                val tvPrice: TextView = it.findViewById(R.id.tvprice)
                val btnAdd: Button = it.findViewById(R.id.btnAddToCart)

                tvName.text = product.pname ?: "N/A"
                tvDesc.text = product.desc ?: "-"
                tvPrice.text = "₹ ${product.price ?: "0"}"

                val detailBitmap = product.pic?.let { pic -> base64ToBitmap(pic) }
                if (detailBitmap != null) imgDetail.setImageBitmap(detailBitmap)
                else imgDetail.setImageResource(R.mipmap.logo_one)

                btnAdd.setOnClickListener {
                    addToCart(product, holder)
                    dialog.dismiss()
                }
            }
        }
    }

    override fun getItemCount(): Int = productList.size

    fun filter(query: String) {
        productList = if (query.isEmpty()) {
            ArrayList(originalList)
        } else {
            val filtered = originalList.filter {
                it.pname?.contains(query, true) == true ||
                        it.price?.contains(query, true) == true
            }
            ArrayList(filtered)
        }
        notifyDataSetChanged()
    }

    fun updateData(newList: ArrayList<ProductModel>) {
        productList = newList
        originalList = ArrayList(newList)
        notifyDataSetChanged()
    }

    private fun base64ToBitmap(base64Str: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // ✅ Add to Cart Logic
    private fun addToCart(product: ProductModel, holder: ProductViewHolder) {
        val cartRef = FirebaseDatabase.getInstance().getReference("cart")

        // Product ID as key (avoid duplicates)
        val productId = product.pid ?: cartRef.push().key!!

        cartRef.child(productId).setValue(product)
            .addOnSuccessListener {
                Toast.makeText(holder.imgProd.context, "Added To Cart ✅", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(holder.imgProd.context, "Failed To Add ❌", Toast.LENGTH_SHORT).show()
            }
    }
}
