package com.krygodev.safenotes.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.krygodev.safenotes.R
import kotlinx.android.synthetic.main.fragment_registration.*


class RegistrationFragment: BaseFragment() {

    private val fbAuth = FirebaseAuth.getInstance()
    private val REG_DEBUG = "REG_DEBUG"
    private val EMAIL_VERIFICATION_DEBUG = "EMAIL_VERIFICATION_DEBUG"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_registration, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createAccountClick()
    }


    private fun createAccountClick() {
        create_account_button.setOnClickListener {
            val email = reg_email_input.text?.trim().toString()
            val password = reg_password_input.text?.trim().toString()
            val repeatPassword = reg_repeat_password_input.text?.trim().toString()

            if (email == "" || password == "" || repeatPassword == "")
                Snackbar.make(requireView(), "Fill in blank fields!", Snackbar.LENGTH_SHORT).show()
            else {

                if (password == repeatPassword) {
                    fbAuth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener { authResult ->
                            val user = authResult.user
                            user!!.sendEmailVerification()
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(context, "Account created! Verify your email address!", Toast.LENGTH_LONG).show()
                                        Log.d(EMAIL_VERIFICATION_DEBUG, "Email sent!")
                                        fbAuth.signOut()
                                        restartApp()
                                    }
                                }
                        }
                        .addOnFailureListener { exc ->
                            Log.d(REG_DEBUG, exc.message.toString())
                            Snackbar.make(requireView(), exc.message.toString(), Snackbar.LENGTH_LONG).show()
                }
                } else {
                    Snackbar.make(requireView(), "Passwords are different!", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }
}