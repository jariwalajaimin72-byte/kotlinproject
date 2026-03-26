package com.example.kotlinproject.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinproject.R

class ReviewFeedbackActivity : AppCompatActivity() {

    private lateinit var ratingBar: RatingBar
    private lateinit var etReview: EditText
    private lateinit var etFeedback: EditText
    private lateinit var btnSubmitReview: Button
    private lateinit var btnSubmitFeedback: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_feedback)

        // Initialize views
        ratingBar = findViewById(R.id.ratingBar)
        etReview = findViewById(R.id.etReview)
        etFeedback = findViewById(R.id.etFeedback)
        btnSubmitReview = findViewById(R.id.btnSubmitReview)
        btnSubmitFeedback = findViewById(R.id.btnSubmitFeedback)

        // Submit Review Button
        btnSubmitReview.setOnClickListener {

            val rating = ratingBar.rating
            val reviewText = etReview.text.toString().trim()

            if (rating == 0f) {
                Toast.makeText(this, "Please select rating", Toast.LENGTH_SHORT).show()
            } else if (reviewText.isEmpty()) {
                etReview.error = "Please write your review"
            } else {

                // TODO: Save review to Firebase here

                Toast.makeText(this, "Review submitted successfully ❤️", Toast.LENGTH_SHORT).show()

                // Clear fields
                ratingBar.rating = 0f
                etReview.text.clear()
            }
        }

        // Submit Feedback Button
        btnSubmitFeedback.setOnClickListener {

            val feedbackText = etFeedback.text.toString().trim()

            if (feedbackText.isEmpty()) {
                etFeedback.error = "Please enter feedback"
            } else {

                // TODO: Save feedback to Firebase here

                Toast.makeText(this, "Thank you for your feedback 💌", Toast.LENGTH_SHORT).show()

                // Clear field
                etFeedback.text.clear()
            }
        }
    }
}