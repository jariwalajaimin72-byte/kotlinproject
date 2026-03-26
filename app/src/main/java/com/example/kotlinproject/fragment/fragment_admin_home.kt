package com.example.kotlinproject.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.kotlinproject.R
import com.example.kotlinproject.activity.AdminAddOtherActivity
import com.example.kotlinproject.activity.AdminShowProductActivity
import com.example.kotlinproject.activity.AdminShowUserActivity
import com.example.kotlinproject.activity.Admin_Show_cat_Activity
import com.google.android.material.card.MaterialCardView

class fragment_admin_home : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_admin_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ------------------- IMAGE SLIDER -------------------
        val imageSlider = view.findViewById<ImageSlider>(R.id.image_slider)

        val imageList = ArrayList<SlideModel>()

        imageList.add(SlideModel(R.drawable.slider1, ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel(R.drawable.slider2, ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel(R.drawable.slider3, ScaleTypes.CENTER_CROP))

        imageSlider.setImageList(imageList)
        imageSlider.startSliding(2000) // auto slide every 2 seconds

        // ------------------- CARDS -------------------
        val cardUsers = view.findViewById<MaterialCardView>(R.id.cardUsers)
        val cardCategory = view.findViewById<MaterialCardView>(R.id.cardCategory)
        val cardProduct = view.findViewById<MaterialCardView>(R.id.cardProduct)
        val cardOthers = view.findViewById<MaterialCardView>(R.id.cardOthers)

        cardUsers.setOnClickListener {
            startActivity(Intent(requireContext(), AdminShowUserActivity::class.java))
        }

        cardCategory.setOnClickListener {
            startActivity(Intent(requireContext(), Admin_Show_cat_Activity::class.java))
        }

        cardProduct.setOnClickListener {
            startActivity(Intent(requireContext(), AdminShowProductActivity::class.java))
        }

        cardOthers.setOnClickListener {
            startActivity(Intent(requireContext(), AdminAddOtherActivity::class.java))
        }
    }
}
