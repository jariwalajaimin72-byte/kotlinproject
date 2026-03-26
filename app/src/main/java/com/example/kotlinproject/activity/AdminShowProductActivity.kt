package com.example.kotlinproject.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinproject.R
import com.example.kotlinproject.adapter.ProductAdapter
import com.example.kotlinproject.model.ProductModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminShowProductActivity : AppCompatActivity() {

    private lateinit var rcylProd: RecyclerView

    private lateinit var add: FloatingActionButton
    private val prodList: ArrayList<ProductModel> = ArrayList()
    val adapter: ProductAdapter = ProductAdapter(prodList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_show_product)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rcylProd = findViewById(R.id.rcylProd)
        add = findViewById(R.id.add)

        rcylProd.layoutManager = GridLayoutManager(this, 2)
        rcylProd.adapter = adapter

        // ✅ Fetch product data
        fetchProduct()

        // Add Product Button
        add.setOnClickListener {
            val intent = Intent(this, AdminAddProductActivity::class.java)
            startActivity(intent)
        }
    }

    fun fetchProduct(){
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading...")
        progressDialog.show()
        val firebaseDatabase= FirebaseDatabase.getInstance()
        val catRef=firebaseDatabase.getReference("product")

        catRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                prodList.clear()
                for (cat in snapshot.children ){
                    val catmodel=cat.getValue(ProductModel::class.java)
                    prodList.add(catmodel!!)
                    adapter!!.notifyDataSetChanged()
                }
                progressDialog.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {
                progressDialog.dismiss()
                Toast.makeText(
                    this@AdminShowProductActivity,
                    "Database error: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
