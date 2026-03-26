package com.example.kotlinproject.adapter

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.TextUtils
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
import com.example.kotlinproject.model.CategoryModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.OnItemClickListener
import com.orhanobut.dialogplus.ViewHolder
import de.hdodenhof.circleimageview.CircleImageView
import com.google.firebase.database.FirebaseDatabase

class CategoryAdapter(
    private val catList: ArrayList<CategoryModel>
) : RecyclerView.Adapter<CategoryAdapter.CatViewHolder>() {

    inner class CatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgCat: ImageView = itemView.findViewById(R.id.imgCat)
        val tvCat: TextView = itemView.findViewById(R.id.tvCat)
        val btnCatEdit: Button = itemView.findViewById(R.id.btnCatEdit)
        val btnCatDelete: Button = itemView.findViewById(R.id.btnCatDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_category, parent, false)
        return CatViewHolder(view)
    }

    override fun onBindViewHolder(holder: CatViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val categoryModel = catList[position]

        holder.tvCat.text = categoryModel.catname ?: "N/A"
        val bitmap = categoryModel.catpic?.takeIf { it.isNotEmpty() }?.let { base64ToBitmap(it) }
        if (bitmap != null) holder.imgCat.setImageBitmap(bitmap)
        else holder.imgCat.setImageResource(R.mipmap.logo_one)

        // Edit Category
        holder.btnCatEdit.setOnClickListener {
            val dialog = DialogPlus.newDialog(holder.imgCat.context)
                .setOnItemClickListener(object : OnItemClickListener {
                    override fun onItemClick(dialog: DialogPlus?, item: Any?, view: View?, position: Int) {}
                })
                .setExpanded(true, 2000)
                .setContentHolder(ViewHolder(R.layout.update_category))
                .create()
            dialog.show()

            val view: View? = dialog.getHolderView()
            val editTextCategory: TextView? = view?.findViewById(R.id.editTextCategory)
            val buttonUpdate: Button? = view?.findViewById(R.id.buttonUpdate)
            val catImg: CircleImageView? = view?.findViewById(R.id.catImg)

            editTextCategory?.text = categoryModel.catname
            catImg?.setImageBitmap(bitmap)

            buttonUpdate?.setOnClickListener {
                val newCatName = editTextCategory?.text.toString()
                if (TextUtils.isEmpty(newCatName)) {
                    Toast.makeText(holder.imgCat.context, "Please enter Category name", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }

                val progressDialog = ProgressDialog(holder.imgCat.context)
                progressDialog.setMessage("Loading...")
                progressDialog.show()

                val id = categoryModel.id ?: ""
                val catRef = FirebaseDatabase.getInstance().getReference("category")
                categoryModel.catname = newCatName
                catRef.child(id).setValue(categoryModel)
                    .addOnSuccessListener {
                        Toast.makeText(holder.imgCat.context, "Updated Successfully!", Toast.LENGTH_LONG).show()
                        notifyItemChanged(position)
                        dialog.dismiss()
                    }
                    .addOnFailureListener {
                        Toast.makeText(holder.imgCat.context, "Update failed!", Toast.LENGTH_LONG).show()
                    }
                    .addOnCompleteListener { progressDialog.dismiss() }
            }
        }

        // Delete Category
        holder.btnCatDelete.setOnClickListener {
            val id = categoryModel.id
            if (id == null) {
                Toast.makeText(holder.imgCat.context, "Invalid category ID", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(holder.imgCat.context)
            materialAlertDialogBuilder.setTitle("Delete")
            materialAlertDialogBuilder.setMessage("Are you sure want to delete?")
            materialAlertDialogBuilder.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
            materialAlertDialogBuilder.setPositiveButton("Yes") { _, _ ->
                val catRef = FirebaseDatabase.getInstance().getReference("category")
                catRef.child(id).removeValue()
                    .addOnSuccessListener {
                        Toast.makeText(holder.imgCat.context, "Deleted", Toast.LENGTH_LONG).show()
                        catList.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, catList.size)
                    }
                    .addOnFailureListener {
                        Toast.makeText(holder.imgCat.context, "Delete failed!", Toast.LENGTH_LONG).show()
                    }
            }
            materialAlertDialogBuilder.show()
        }
    }

    override fun getItemCount(): Int = catList.size

    private fun base64ToBitmap(base64Str: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
