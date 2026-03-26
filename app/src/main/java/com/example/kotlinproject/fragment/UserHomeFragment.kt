package com.example.kotlinproject.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.kotlinproject.R
import com.example.kotlinproject.activity.HomeActivity
import com.example.kotlinproject.adapter.CategoryUserAdapter
import com.example.kotlinproject.adapter.ProductUserAdapter
import com.example.kotlinproject.model.CategoryModel
import com.example.kotlinproject.model.ProductModel
import com.google.firebase.database.*

class UserHomeFragment : Fragment() {

    private lateinit var imageSlider: ImageSlider
    private lateinit var rcylCat: RecyclerView
    private lateinit var rcylProd: RecyclerView
    private lateinit var ivMenu: ImageView

    private val catList = ArrayList<CategoryModel>()
    private val prodList = ArrayList<ProductModel>()
    private lateinit var adapterCat: CategoryUserAdapter
    private lateinit var adapterProd: ProductUserAdapter

    private var progressDialog: ProgressDialog? = null
    private var categoryLoaded = false
    private var productLoaded = false
    private lateinit var etSearch: EditText
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageSlider = view.findViewById(R.id.image_slider)
        rcylCat = view.findViewById(R.id.rcylCat)
        rcylProd = view.findViewById(R.id.rcylProd)
        etSearch = view.findViewById(R.id.etSearch)

        ivMenu = view.findViewById(R.id.ivMenu)
        etSearch.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapterProd.filter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        ivMenu.setOnClickListener {
            (activity as? HomeActivity)?.openDrawer()
        }


        // Image Slider
        val imageList = listOf(
            SlideModel(R.drawable.slideru1, ScaleTypes.FIT),
            SlideModel(R.drawable.slideruu, ScaleTypes.FIT),
            SlideModel(R.drawable.slideru2, ScaleTypes.FIT),
            SlideModel(R.drawable.slideruu2, ScaleTypes.FIT)
        )
        imageSlider.setImageList(imageList)

        // Category RecyclerView
        adapterCat = CategoryUserAdapter(catList)
        rcylCat.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rcylCat.adapter = adapterCat
        rcylCat.isNestedScrollingEnabled = false  // ✅ Important

        // Product RecyclerView
        adapterProd = ProductUserAdapter(prodList)
        rcylProd.layoutManager = GridLayoutManager(context, 2)
        rcylProd.adapter = adapterProd
        rcylProd.isNestedScrollingEnabled = false  // ✅ Important

        // Progress dialog
        progressDialog = ProgressDialog(requireContext()).apply {
            setMessage("Loading...")
            setCancelable(false)
            show()
        }

        fetchCategory()
        fetchProduct()
    }

    private fun fetchCategory() {
        val cateRef = FirebaseDatabase.getInstance().getReference("category")
        cateRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                catList.clear()
                for (cat in snapshot.children) {
                    cat.getValue(CategoryModel::class.java)?.let { catList.add(it) }
                }
                adapterCat.notifyDataSetChanged()
                categoryLoaded = true
                dismissProgressWhenReady()
            }

            override fun onCancelled(error: DatabaseError) {
                categoryLoaded = true
                dismissProgressWhenReady()
                Toast.makeText(context, "Category load failed: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchProduct() {
        val prodRef = FirebaseDatabase.getInstance().getReference("product")
        prodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                prodList.clear()
                for (prod in snapshot.children) {
                    prod.getValue(ProductModel::class.java)?.let { prodList.add(it) }
                }
                adapterProd.updateData(prodList)
                productLoaded = true
                dismissProgressWhenReady()
            }

            override fun onCancelled(error: DatabaseError) {
                productLoaded = true
                dismissProgressWhenReady()
                Toast.makeText(context, "Product load failed: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun dismissProgressWhenReady() {
        if (categoryLoaded && productLoaded) {
            progressDialog?.dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        progressDialog?.dismiss()
    }
}