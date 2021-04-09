package com.krygodev.safenotes.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.krygodev.safenotes.R
import com.krygodev.safenotes.data.User
import com.krygodev.safenotes.viewmodels.LoginViewModel
import kotlinx.android.synthetic.main.fragment_login.*


// Main screen showing after splash screen if user is not logged in
// Responsible for holding signing in, registration and resetting password
@Suppress("DEPRECATION")
class LoginFragment: BaseFragment() {

    private lateinit var fbAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private val loginViewModel by viewModels<LoginViewModel>()

    private val GOOGLE_SIGN_IN_REQUEST_CODE = 0
    private val LOG_DEBUG = "LOG_DEBUG"
    private val GOOGLE_LOG_DEBUG = "GOOGLE_DEBUG"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Setting onClicks
        loginClick()
        googleSignInClick()
        registrationClick()
        resetPasswordClick()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        fbAuth = FirebaseAuth.getInstance()
    }


    // Holding login with email and password
    private fun loginClick() {
        login_button.setOnClickListener {
            val email = email_address_input.text?.trim().toString()
            val password = password_input.text?.trim().toString()

            if (email == "" || password == "")
                Snackbar.make(requireView(), "Fill in blank fields!", Snackbar.LENGTH_SHORT).show()
            else {
                fbAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener { authResult ->
                        val user = authResult.user!!
                        if (user.isEmailVerified) createProgressDialog()
                        else Snackbar.make(requireView(), "Verify your email address!", Snackbar.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { exc ->
                        Snackbar.make(requireView(), exc.message.toString(), Snackbar.LENGTH_LONG).show()
                        Log.d(LOG_DEBUG, exc.message.toString())
                    }
            }
        }
    }


    // Creating GoogleSignIn intent
    private fun googleSignInClick() {
        sign_in_google_button.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, GOOGLE_SIGN_IN_REQUEST_CODE)
        }
    }


    // Navigate to Registration Fragment
    private fun registrationClick() {
        register_button.setOnClickListener {
            findNavController()
                .navigate(LoginFragmentDirections.actionLoginFragmentToRegistrationFragment()
                    .actionId)
        }
    }


    // Navigate to Reset Password Fragment
    private fun resetPasswordClick() {
        reset_password.setOnClickListener {
            findNavController()
                .navigate(LoginFragmentDirections.actionLoginFragmentToResetPasswordFragment()
                    .actionId)
        }
    }


    // Getting result from GoogleSignIn intent
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN_REQUEST_CODE) {
            GoogleSignIn.getSignedInAccountFromIntent(data)
                .addOnSuccessListener {
                    fbAuthWithGoogle(it.idToken!!)
                }
                .addOnFailureListener { exc ->
                    Snackbar.make(requireView(), exc.message.toString(), Snackbar.LENGTH_SHORT)
                    Log.d(GOOGLE_LOG_DEBUG, exc.message.toString())
                }
        }
    }


    // Signing in with Google credentials and creating new user in FireStore
    private fun fbAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        fbAuth.signInWithCredential(credential)
            .addOnSuccessListener {
                val fbUser = fbAuth.currentUser
                val user = User(fbUser!!.uid, fbUser.displayName!!.substring(0, fbUser.displayName!!.indexOf(" ")), fbUser.email)
                loginViewModel.createNewGoogleSignInUser(user)
                createProgressDialog()
            }
            .addOnFailureListener { exc ->
                Snackbar.make(requireView(), exc.message.toString(), Snackbar.LENGTH_SHORT)
                Log.d(GOOGLE_LOG_DEBUG, exc.message.toString())
            }
    }


    // Sign in progress dialog builder
    private fun createProgressDialog() {
        AlertDialog.Builder(requireContext())
            .setCancelable(false)
            .setView(R.layout.progress_dialog_login)
            .create()
            .show()

        Handler().postDelayed(signIn, 1500)
    }


    private val signIn = Runnable {
        startApp()
    }
}