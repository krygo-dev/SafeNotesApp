package com.krygodev.safenotes.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.krygodev.safenotes.R
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment: BaseFragment() {

    private val fbAuth = FirebaseAuth.getInstance()
    private lateinit var googleApiClient: GoogleApiClient
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
        loginClick()
        googleSignInClick()
        registrationClick()
        resetPasswordClick()
    }


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
                        if (user.isEmailVerified) startApp()
                        else Snackbar.make(requireView(), "Verify your email address!", Snackbar.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { exc ->
                        Snackbar.make(requireView(), exc.message.toString(), Snackbar.LENGTH_LONG).show()
                        Log.d(LOG_DEBUG, exc.message.toString())
                    }
            }
        }
    }


    private fun googleSignInClick() {
        sign_in_google_button.setOnClickListener {
            initGoogleSignIn()
            signInWithGoogle()
        }
    }


    private fun registrationClick() {
        register_button.setOnClickListener {
            findNavController()
                .navigate(LoginFragmentDirections.actionLoginFragmentToRegistrationFragment()
                    .actionId)
        }
    }


    private fun resetPasswordClick() {
        reset_password.setOnClickListener {
            findNavController()
                .navigate(LoginFragmentDirections.actionLoginFragmentToResetPasswordFragment()
                    .actionId)
        }
    }


    private fun initGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleApiClient = context?.let { context ->
            activity?.let { activity ->
                GoogleApiClient.Builder(context)
                    .enableAutoManage(activity) { connectionResult ->
                        Snackbar.make(requireView(), "Google sign in failed!", Snackbar.LENGTH_SHORT)
                        Log.d(GOOGLE_LOG_DEBUG, connectionResult.errorMessage.toString())
                    }
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build()
            }
        }!!
    }


    private fun signInWithGoogle() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN_REQUEST_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN_REQUEST_CODE) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)

            if (result != null && result.isSuccess) result.signInAccount?.let { fbAuthWithGoogle(it) }
        }
    }


    private fun fbAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        fbAuth.signInWithCredential(credential)
            .addOnSuccessListener {
                startApp()
            }
            .addOnFailureListener { exc ->
                Snackbar.make(requireView(), exc.message.toString(), Snackbar.LENGTH_SHORT)
                Log.d(GOOGLE_LOG_DEBUG, exc.message.toString())
            }
    }
}