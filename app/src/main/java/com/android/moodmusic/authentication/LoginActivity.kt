package com.android.moodmusic.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import com.android.moodmusic.R
import com.android.moodmusic.dashboard.CreatePlaylistActivity
import com.android.moodmusic.dashboard.ParentPlaylistMoodActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: TextInputEditText
    private lateinit var etPass: TextInputEditText
    private lateinit var btnLogin: Button
    private lateinit var notHaveAccounLL: LinearLayout
    private lateinit var  loadingAnimation : LottieAnimationView

    // Creating firebaseAuth object
    private lateinit var auth: FirebaseAuth

    override fun onStart() {
        // initialising Firebase auth object

        if(auth.currentUser != null) {
            var intent = Intent(this@LoginActivity, ParentPlaylistMoodActivity::class.java)
            startActivity(intent)
            finish()
        }
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // initializing with ids
        etEmail = findViewById(R.id.et_mail)
        etPass = findViewById(R.id.et_uid)
        btnLogin = findViewById(R.id.btn_login)
        notHaveAccounLL = findViewById(R.id.ll_not_have_account)
        loadingAnimation = findViewById(R.id.animationView)

        // initialising Firebase auth object
        auth = FirebaseAuth.getInstance()

        btnLogin.setOnClickListener {
            login()
        }

        notHaveAccounLL.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            // using finish() to end the activity
            finish()
        }
    }

    private fun login() {
        val email = etEmail.text?.trim().toString()
        val pass = etPass.text?.trim().toString()

        if(email.isEmpty() || !(Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            Toast.makeText(applicationContext, "Please enter valid email address", Toast.LENGTH_SHORT).show()
            return
        }else if(pass.isEmpty() || pass.length < 8) {
            Toast.makeText(applicationContext, "Please enter strong password", Toast.LENGTH_SHORT).show()
            return
        }
        loadingAnimation.visibility = View.VISIBLE

        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                loadingAnimation.visibility = View.VISIBLE
                Toast.makeText(this, "Successfully LoggedIn", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, ParentPlaylistMoodActivity::class.java))
                finish()
            } else {
                loadingAnimation.visibility = View.GONE
                Toast.makeText(this, "Log In failed " +  it.exception?.message, Toast.LENGTH_SHORT).show()
                Log.d("LOGIN", "Log In failed ${it.exception?.message}")
            }
        }
    }
}