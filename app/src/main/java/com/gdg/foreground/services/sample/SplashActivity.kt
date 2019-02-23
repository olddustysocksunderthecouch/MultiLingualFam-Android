package com.gdg.foreground.services.sample

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log

import com.gdg.foreground.services.sample.auth.AuthenticationActivity
import com.google.firebase.auth.FirebaseAuth


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser

        if (currentUser == null) {
            Log.d("SplashActivity", "User is null")
            val intent = Intent(this@SplashActivity, AuthenticationActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
            Log.d("SplashActivity", "User is signed in")
        }


    }
}
