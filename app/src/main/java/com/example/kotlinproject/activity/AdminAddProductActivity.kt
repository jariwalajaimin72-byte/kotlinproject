package com.example.kotlinproject.activity

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinproject.R
import com.example.kotlinproject.model.CategoryModel
import com.example.kotlinproject.model.ProductModel
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream

class AdminAddProductActivity : AppCompatActivity() {

    private lateinit var pImg: CircleImageView
    private lateinit var editTextPname: EditText
    private lateinit var editTextPrice: EditText
    private lateinit var editTextPdesc: EditText
    private lateinit var spCat: Spinner
    private lateinit var buttonAdd: Button
    private lateinit var btnChoose: Button

    private var bitmap: Bitmap? = null
    private var category = ""
    private val categoryArrayList = ArrayList<CategoryModel>()
    private val catnameList = ArrayList<String>()

    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_add_product)

        // Initialize views
        pImg = findViewById(R.id.pImg)
        editTextPname = findViewById(R.id.editTextPname)
        editTextPrice = findViewById(R.id.editTextPrice)
        editTextPdesc = findViewById(R.id.editTextPdesc)
        spCat = findViewById(R.id.spCat)
        buttonAdd = findViewById(R.id.buttonAdd)
        btnChoose = findViewById(R.id.btnChoose)

        progressDialog = ProgressDialog(this).apply {
            setMessage("Loading...")
            setCancelable(false)
        }

        btnChoose.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start()
        }

        fetchCategory()

        spCat.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                category = catnameList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        buttonAdd.setOnClickListener {
            val etname = editTextPname.text.toString().trim()
            val etprice = editTextPrice.text.toString().trim()
            val desc = editTextPdesc.text.toString().trim()
            val categoryName = category

            if (etname.isEmpty() || etprice.isEmpty() || desc.isEmpty() || categoryName.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else if (bitmap == null) {
                Toast.makeText(this, "Please choose an image", Toast.LENGTH_SHORT).show()
            } else {
                progressDialog?.show()
                val prodRef = FirebaseDatabase.getInstance().getReference("product")
                val pid = prodRef.push().key
                val pic = imageToBase64(bitmap!!)

                val productModel = ProductModel(pid, etname, etprice, pic, desc, categoryName)
                prodRef.child(pid!!).setValue(productModel)
                    .addOnSuccessListener {
                        progressDialog?.dismiss()
                        Toast.makeText(this, "Product added successfully!", Toast.LENGTH_LONG).show()
                        finish()
                    }
                    .addOnFailureListener {
                        progressDialog?.dismiss()
                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
                    }
            }
        }
    }

    private fun fetchCategory() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading categories...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val catRef = FirebaseDatabase.getInstance().getReference("category")
        catRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categoryArrayList.clear()
                catnameList.clear()

                for (cat in snapshot.children) {
                    val catModel = cat.getValue(CategoryModel::class.java)
                    if (catModel != null) {
                        categoryArrayList.add(catModel)
                        catnameList.add(catModel.catname ?: "")
                    }
                }

                val adapter = ArrayAdapter(
                    this@AdminAddProductActivity,
                    android.R.layout.simple_list_item_1,
                    catnameList
                )
                spCat.adapter = adapter

                progressDialog.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {
                progressDialog.dismiss()
                Toast.makeText(this@AdminAddProductActivity, "Failed to load categories", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            val uri: Uri? = data.data
            pImg.setImageURI(uri)

            try {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun imageToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }

    override fun onDestroy() {
        super.onDestroy()
        progressDialog?.dismiss()
    }
}
