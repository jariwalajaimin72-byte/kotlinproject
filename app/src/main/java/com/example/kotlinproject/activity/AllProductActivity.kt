package com.example.kotlinproject

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinproject.adapter.ProductUserAdapter
import com.example.kotlinproject.model.ProductModel
import com.google.firebase.database.*

class AllProductActivity : AppCompatActivity() {

    private val prodList = ArrayList<ProductModel>()
    private var adapter1: ProductUserAdapter? = null
    private lateinit var rcylProd: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_all_product)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rcylProd = findViewById(R.id.rcylProd)
        adapter1 = ProductUserAdapter(prodList)
        rcylProd.layoutManager = GridLayoutManager(this, 2)
        rcylProd.adapter = adapter1

        val category = intent.getStringExtra("category") ?: ""
        if (category.isEmpty()) {
            Toast.makeText(this, "Category not found", Toast.LENGTH_SHORT).show()
        } else {
            fetchProduct(category)
        }
    }

    private fun fetchProduct(category: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading products...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val catRef = FirebaseDatabase.getInstance().getReference("product")
        catRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                prodList.clear()
                for (cat in snapshot.children) {
                    val catmodel = cat.getValue(ProductModel::class.java)
                    catmodel?.let {
                        if (it.category == category) {
                            prodList.add(it)
                        }
                    }
                }
                adapter1?.notifyDataSetChanged()
                progressDialog.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {
                progressDialog.dismiss()
                Toast.makeText(this@AllProductActivity, "Database error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
