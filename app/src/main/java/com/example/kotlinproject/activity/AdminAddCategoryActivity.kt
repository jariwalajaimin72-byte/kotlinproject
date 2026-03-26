package com.example.kotlinproject.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinproject.R
import com.example.kotlinproject.model.CategoryModel
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.database.FirebaseDatabase
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream

class AdminAddCategoryActivity : AppCompatActivity() {

    private lateinit var editTextCategory: EditText
    private lateinit var btnChoose: Button
    private lateinit var buttonAdd: Button
    private lateinit var catImg: CircleImageView
    private var bitmap: Bitmap? = null

    // ✅ New way: ActivityResult launcher
    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val uri = data?.data
            catImg.setImageURI(uri)  // preview image
            try {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
            }
        } else if (result.resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(result.data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_add_category)

        // Bind views
        editTextCategory = findViewById(R.id.editTextCategory)
        btnChoose = findViewById(R.id.btnChoose)
        buttonAdd = findViewById(R.id.buttonAdd)
        catImg = findViewById(R.id.catImg)

        // Choose Image
        btnChoose.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .createIntent { intent -> imagePickerLauncher.launch(intent) }
        }

        // Add button
        buttonAdd.setOnClickListener {
            val catname = editTextCategory.text.toString().trim()

            if (catname.isEmpty()) {
                Toast.makeText(this, "Please enter category name", Toast.LENGTH_SHORT).show()
            } else if (bitmap == null) {
                Toast.makeText(this, "Please choose an image", Toast.LENGTH_SHORT).show()
            } else {
                val catpic = imageToBase64(bitmap!!)
                saveCategory(catname, catpic)
            }
        }
    }

    // Save category in Firebase
    private fun saveCategory(catname: String, catpic: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Saving...")
        progressDialog.show()

        val firebaseDatabase = FirebaseDatabase.getInstance()
        val catRef = firebaseDatabase.getReference("category")
        val catid = catRef.push().key
        val categoryModel = CategoryModel(catid, catname, catpic)

        catid?.let {
            catRef.child(it).setValue(categoryModel)
                .addOnSuccessListener {
                    progressDialog.dismiss()
                    Toast.makeText(this, "Category Inserted", Toast.LENGTH_SHORT).show()
                    editTextCategory.text.clear()
                    catImg.setImageResource(R.mipmap.logo_one)
                    bitmap = null
                }
                .addOnFailureListener {
                    progressDialog.dismiss()
                    Toast.makeText(this, "Insert Failed", Toast.LENGTH_SHORT).show()
                }
        }
    }

    // Convert to Base64
    private fun imageToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}
