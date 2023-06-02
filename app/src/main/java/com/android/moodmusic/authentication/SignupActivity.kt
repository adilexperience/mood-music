package com.android.moodmusic.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import com.android.moodmusic.R
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {
    private lateinit var etEmail: TextInputEditText
    private lateinit var etConfPass: TextInputEditText
    private lateinit var etPass: TextInputEditText
    private lateinit var btnSignUp: Button
    private lateinit var llAlreadyHaveAccount: LinearLayout
    private lateinit var  loadingAnimation : LottieAnimationView

    // create Firebase authentication object
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // View Bindings
        etEmail = findViewById(R.id.et_mail)
        etConfPass = findViewById(R.id.et_rep_uid)
        etPass = findViewById(R.id.et_uid)
        btnSignUp = findViewById(R.id.btn_register)
        llAlreadyHaveAccount = findViewById(R.id.ll_have_account)
        loadingAnimation = findViewById(R.id.animationView)

        // Initialising auth object
        auth = FirebaseAuth.getInstance()

        btnSignUp.setOnClickListener {
            signUpUser()
        }

        // switching from signUp Activity to Login Activity
        llAlreadyHaveAccount.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun signUpUser() {
        val email = etEmail.text?.trim().toString()
        val pass = etPass.text?.trim().toString()
        val confirmPassword = etConfPass.text?.trim().toString()

        // check pass
        if(email.isEmpty() || !(Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            Toast.makeText(applicationContext, "Please enter valid email address", Toast.LENGTH_SHORT).show()
            return
        }else if(pass.isEmpty() || pass.length < 8) {
            Toast.makeText(applicationContext, "Please enter strong password", Toast.LENGTH_SHORT).show()
            return
        }else if(pass != confirmPassword) {
            Toast.makeText(applicationContext, "Both password must be same", Toast.LENGTH_SHORT).show()
            return
        }

        loadingAnimation.visibility = View.VISIBLE
        // If all credential are correct
        // We call createUserWithEmailAndPassword
        // using auth object and pass the
        // email and pass in it.
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                // add initial values to user profile
                val db = Firebase.firestore
                val user = hashMapOf(
                    "email_address" to email,
                    "id" to auth.currentUser!!.uid,
                )
                db.collection("Users").document(auth.currentUser!!.uid).set(user).addOnSuccessListener {
                    loadingAnimation.visibility = View.GONE
                    Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }.addOnFailureListener(OnFailureListener {
                    loadingAnimation.visibility = View.GONE
                    Log.e("CREATE_PROFILE", "Data failed to be added in firestore")
                    Toast.makeText(this, "Account creation failed", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                })
            } else {
                loadingAnimation.visibility = View.GONE
                Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

}