package com.example.mindmoneychallenge.Registeration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.mindmoneychallenge.DataAndModelClass.UserSignup
import com.example.mindmoneychallenge.MainActivity
import com.example.mindmoneychallenge.databinding.ActivitySignupBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private var userKey: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupBtn.setOnClickListener {
            val name: String = binding.userNameSignup.text.toString().trim()
            val age: String = binding.ageSignup.text.toString().trim()
            val email: String = binding.emailSignup.text.toString().trim()
            val password: String = binding.passwordSignup.text.toString().trim()

            if (name.isEmpty()) {
                binding.userNameSignup.error = "Field can't be empty"
            } else if (age.isEmpty()) {
                binding.ageSignup.error = "Field can't be empty"
            } else if (email.isEmpty()) {
                binding.emailSignup.error = "Field can't be empty"
            } else if (password.isEmpty()) {
                binding.passwordSignup.error = "Field can't be empty"
            } else if (age.length >= 3) {
                binding.ageSignup.error = "Enter correct Age"
            } else {
                registerUser(email, password, email, password, age, name)
                binding.progressBar.visibility = View.VISIBLE
            }
        }
    }

    private fun registerUser(
        email: String,
        password: String,
        email_db: String,
        password_db: String,
        age_db: String,
        userName_db: String
    ) {
        Firebase.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                userKey = it.result.user!!.uid  //To get UserId
                val user = UserSignup(userName_db, age_db, email_db, password_db)
                Firebase.database.reference.child("Users").child(Firebase.auth.currentUser!!.uid)
//                    .push()
                    .setValue(user).addOnSuccessListener {
                        binding.progressBar.visibility = View.INVISIBLE
                        Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this,MainActivity::class.java))
                    }
            } else {
                Toast.makeText(this, it.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (Firebase.auth.currentUser != null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }
}