package com.example.kotlinproject.adapter

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinproject.AllProductActivity
import com.example.kotlinproject.R
import com.example.kotlinproject.model.CategoryModel
import de.hdodenhof.circleimageview.CircleImageView

class CategoryUserAdapter(
    private val categoryList: ArrayList<CategoryModel>
) : RecyclerView.Adapter<CategoryUserAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgCat: CircleImageView = itemView.findViewById(R.id.imgCat)
        val tvCatName: TextView = itemView.findViewById(R.id.tvCatName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_user_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoryList[position]

        // Set category name safely
        holder.tvCatName.text = category.catname ?: "No Name"

        // Set category image safely
        val bitmap = category.catpic?.let { base64ToBitmap(it) }
        if (bitmap != null) {
            holder.imgCat.setImageBitmap(bitmap)
        } else {
            holder.imgCat.setImageResource(R.mipmap.logo_one) // default image if null
        }

        // Handle item click safely
        holder.itemView.setOnClickListener {
            category.catname?.let { catName ->
                val intent = Intent(holder.itemView.context, AllProductActivity::class.java)
                intent.putExtra("category", catName)
                holder.itemView.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = categoryList.size

    // Convert base64 string to Bitmap safely
    private fun base64ToBitmap(base64Str: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            null
        }
    }
}
