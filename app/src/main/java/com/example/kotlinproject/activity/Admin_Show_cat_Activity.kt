package com.example.kotlinproject.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinproject.R
import com.example.kotlinproject.adapter.CategoryAdapter
import com.example.kotlinproject.model.CategoryModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

class Admin_Show_cat_Activity : AppCompatActivity() {

    private lateinit var fabAddCat: FloatingActionButton
    private lateinit var reclycat: RecyclerView

    private var adapter: CategoryAdapter? = null
    private val categoryArrayList: ArrayList<CategoryModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_show_cat)

        // Safe window insets handling
        findViewById<android.view.View>(R.id.main)?.let { rootView ->
            ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }

        fabAddCat = findViewById(R.id.fabAddCat)
        reclycat = findViewById(R.id.rcylCat)

        fabAddCat.setOnClickListener {
            val intent = Intent(this, AdminAddCategoryActivity::class.java)
            startActivity(intent)
        }

        adapter = CategoryAdapter(categoryArrayList)
        reclycat.layoutManager = LinearLayoutManager(this)
        reclycat.adapter = adapter

        fetchCategory()
    }

    private fun fetchCategory() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val cateRef = FirebaseDatabase.getInstance().getReference("category")
        cateRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categoryArrayList.clear()
                for (cat in snapshot.children) {
                    val categoryModel = cat.getValue(CategoryModel::class.java)
                    categoryModel?.let { categoryArrayList.add(it) }
                }
                adapter?.notifyDataSetChanged()
                progressDialog.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {
                progressDialog.dismiss()
                Toast.makeText(
                    this@Admin_Show_cat_Activity,
                    "Database error: ${error.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}