package com.example.mindmoneychallenge.Result

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mindmoneychallenge.MainActivity
import com.example.mindmoneychallenge.R
import com.example.mindmoneychallenge.databinding.ActivityResultsBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ResultsActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityResultsBinding.inflate(layoutInflater)
    }
    private var currentChance: Long = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val total = intent.getIntExtra("total", 5)

        Firebase.database.reference.child("PlayChance").child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            currentChance = snapshot.value as Long
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                }
            )

        if (total >= 100) {
            binding.resultsAnimation.setAnimation(R.raw.winn)
            binding.resultsAnimation.playAnimation()
            binding.resultText.text = "Winner"

            var isUpdated = false

            if (isUpdated) {

            } else {
                Firebase.database.reference.child("PlayChance")
                    .child(Firebase.auth.currentUser!!.uid)
                    .setValue(currentChance + 1).addOnSuccessListener {
                        binding.score.text = currentChance.toString()
                        isUpdated = true
                    }
            }


        } else {
            binding.resultsAnimation.setAnimation(R.raw.failed)
            binding.resultsAnimation.playAnimation()
            binding.resultText.text = "Best Luck Next Time"
        }

        binding.playAgainButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
    }
}