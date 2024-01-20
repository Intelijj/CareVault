package com.example.carevault

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class AppFeedbackActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_feedback)

        if (auth.currentUser == null) {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
            return
        }

        val userId = auth.currentUser?.uid
        val backButton = findViewById<ImageButton>(R.id.imageButton112)
        val submitButton = findViewById<TextView>(R.id.textView5ss)

        backButton.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        submitButton.setOnClickListener {
            val editFeedButton = findViewById<TextView>(R.id.textView11116).text.toString()

            userId?.let {
                val feedBackData = db.collection("Users").document(userId).collection("Feedback")

                val feeddata = hashMapOf(
                    "Feedback" to editFeedButton,
                    "Timestamp" to FieldValue.serverTimestamp()
                )

                feedBackData.add(feeddata)
                    .addOnSuccessListener {
                        Toast.makeText(
                            applicationContext,
                            "Feedback saved successfully",
                            Toast.LENGTH_SHORT
                        ).show()

                        startActivity(Intent(this, ProfileActivity::class.java))
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            applicationContext,
                            "Error saving feedback: $e",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }
    }
}