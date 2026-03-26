package com.example.kotlinproject.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinproject.R
import com.example.kotlinproject.adapter.CategoryUserAdapter
import com.example.kotlinproject.model.CategoryModel
import com.google.firebase.database.*

class UserCategoryFragment : Fragment() {

    private lateinit var rcylCat: RecyclerView
    private val catList = ArrayList<CategoryModel>()
    private lateinit var adapterCat: CategoryUserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rcylCat = view.findViewById(R.id.rcylCat)
        adapterCat = CategoryUserAdapter(catList)

        // Grid layout (2 columns)
        rcylCat.layoutManager = GridLayoutManager(context, 2)
        rcylCat.setHasFixedSize(true)
        rcylCat.adapter = adapterCat

        fetchAllCategory()
    }

    private fun fetchAllCategory() {
        context?.let { ctx ->
            val progressDialog = ProgressDialog(ctx)
            progressDialog.setMessage("Loading categories...")
            progressDialog.setCancelable(false)
            progressDialog.show()

            val cateRef = FirebaseDatabase.getInstance().getReference("category")
            cateRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    catList.clear()
                    for (cat in snapshot.children) {
                        cat.getValue(CategoryModel::class.java)?.let {
                            catList.add(it)
                        }
                    }
                    adapterCat.notifyDataSetChanged()
                    progressDialog.dismiss()
                }

                override fun onCancelled(error: DatabaseError) {
                    progressDialog.dismiss()
                    Toast.makeText(ctx, "Failed to load categories: ${error.message}", Toast.LENGTH_SHORT).show()}
            })
        }
    }
}
