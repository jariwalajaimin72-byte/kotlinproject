package com.example.kotlinproject.activity

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinproject.R
import com.example.kotlinproject.fragment.UserCartFragment
import com.example.kotlinproject.model.ProductModel
import com.google.firebase.database.FirebaseDatabase

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var imgProd: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvDesc: TextView
    private lateinit var tvPrice: TextView
    private lateinit var btnAdd: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        imgProd = findViewById(R.id.imgProd)
        tvName = findViewById(R.id.tvpname)
        tvDesc = findViewById(R.id.tvpdesc)
        tvPrice = findViewById(R.id.tvprice)
        btnAdd = findViewById(R.id.btnAddToCart)

        // Get product data from intent
        val name = intent.getStringExtra("name")
        val desc = intent.getStringExtra("description")
        val price = intent.getStringExtra("price")
        val image = intent.getStringExtra("image")

        tvName.text = name
        tvDesc.text = desc
        tvPrice.text = "₹ $price"

        if (!image.isNullOrEmpty()) {
            val decodedBytes = Base64.decode(image, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            imgProd.setImageBitmap(bitmap)
        } else {
            imgProd.setImageResource(R.mipmap.logo_one)
        }

        btnAdd.setOnClickListener {
            val productId = FirebaseDatabase.getInstance().reference.push().key ?: ""

            val product = ProductModel(
                pid = productId,
                pname = name,
                desc = desc,
                price = price,
                pic = image
            )

            val cartRef = FirebaseDatabase.getInstance().getReference("cart")
            cartRef.child(productId).setValue(product)
                .addOnSuccessListener {
                    Toast.makeText(this, "Added to Cart ✅", Toast.LENGTH_SHORT).show()
                    // 👉 Redirect to CartActivity
                    startActivity(Intent(this, UserCartFragment::class.java))
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to add ❌", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
