package com.gdg.foreground.services.sample.auth

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.gdg.foreground.services.sample.MainActivity
import com.gdg.foreground.services.sample.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.android.synthetic.main.activity_authentication.*
import java.util.*

/**
 * Created by Adrian Bunge on 2018/09/08.
 */

class AuthenticationActivity : BaseActivity() {

    private var mAuth: FirebaseAuth? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        var isNewRegistration = false

        emailCreateAccountButton.setOnClickListener {
            welcomeConstraintLayout.visibility = View.GONE
            detailsContraintLayout.visibility = View.VISIBLE
            nameEditText.visibility = View.VISIBLE
            isNewRegistration = true
        }
        emailSignInButton.setOnClickListener {
            welcomeConstraintLayout.visibility = View.GONE
            detailsContraintLayout.visibility = View.VISIBLE
            nameEditText.visibility = View.GONE
            isNewRegistration = false
        }

        goButton.setOnClickListener {
            if (isNewRegistration) createAccount(nameEditText.text.toString(), emailEditText.text.toString(), passwordEditText.text.toString())
            else signIn(emailEditText.text.toString(), passwordEditText.text.toString())
        }

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance()
        // [END initialize_auth]
    }

    // [START on_start_check_user]
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth!!.currentUser
        updateUI(currentUser)
    }
    // [END on_start_check_user]

    private fun createAccount(name: String, email: String, password: String) {
        Log.d(TAG, "createAccount:$email")
        if (!validateForm()) {
            return
        }

        showProgressDialog()

        // [START create_user_with_email]
        mAuth!!.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")

                        val user = mAuth!!.currentUser
                        updateFirebaseDisplayName(name, user!!)
                        createUserAccount(user.uid, name)
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(this@AuthenticationActivity, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }

                    // [START_EXCLUDE]
                    hideProgressDialog()
                    // [END_EXCLUDE]
                }
        // [END create_user_with_email]
    }

    private fun updateFirebaseDisplayName(name: String, user: FirebaseUser){
        val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()

//        user.updateProfile(profileUpdates)
//                .addOnCompleteListener()
    }

    private fun createUserAccount(uid: String, name: String) {
        Log.e("Create New Account", "Cloud Function Called")
        // Create the arguments to the callable function.
        val data = HashMap<String, Any>()
        data["display_name"] = name
        data["uid"] = uid
        data["profile_pic"] = "https://firebasestorage.googleapis.com/v0/b/chitchat-2faa0.appspot.com/o/1_sgte14nnEGB1cwlDbjbBrw.png?alt=media&token=1d39cad9-1030-45ab-b5a6-c1c4f8e1c8c5"

        FirebaseFunctions.getInstance()
                .getHttpsCallable("createAccount")
                .call(data)
                .continueWith { task ->
                    task.result?.data as String
                }
    }


    private fun signIn(email: String, password: String) {
        Log.d(TAG, "signIn:$email")
        if (!validateForm()) {
            return
        }

        showProgressDialog()

        // [START sign_in_with_email]
        mAuth!!.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = mAuth!!.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(this@AuthenticationActivity, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }
                }
        // [END sign_in_with_email]
    }

    private fun validateForm(): Boolean {
        var valid = true

        val email = emailEditText.text.toString()
        if (TextUtils.isEmpty(email)) {
            emailEditText.error = "Required."
            valid = false
        } else {
            emailEditText.error = null
        }

        val password = passwordEditText.text.toString()
        if (TextUtils.isEmpty(password)) {
            passwordEditText.error = "Required."
            valid = false
        } else {
            passwordEditText.error = null
        }

        return valid
    }

    private fun updateUI(user: FirebaseUser?) {
        hideProgressDialog()
        if (user != null) {
            val startMainActivity = Intent(this@AuthenticationActivity, MainActivity::class.java)
            this@AuthenticationActivity.startActivity(startMainActivity)
        }
    }

    companion object {

        private val TAG = "AuthenticationActivity"
    }
}