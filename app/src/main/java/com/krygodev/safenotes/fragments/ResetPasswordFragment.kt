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
import kotlinx.android.synthetic.main.fragment_reset_password.*


class ResetPasswordFragment: BaseFragment() {

    private val fbAuth = FirebaseAuth.getInstance()
    private val RESET_PASS_DEBUG = "RESET_PASSWORD_DEBUG"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reset_password, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resetPasswordClick()
    }


    private fun resetPasswordClick() {
        reset_password_button.setOnClickListener {
            val email = reset_password_email_input.text?.trim().toString()

            if (email == "")
                Snackbar.make(requireView(), "Fill in blank field!", Snackbar.LENGTH_SHORT).show()
            else {
                fbAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "Link to reset password sent!", Toast.LENGTH_LONG).show()
                            Log.d(RESET_PASS_DEBUG, "Email sent!")
                            restartApp()
                        }
                    }
                    .addOnFailureListener { exc ->
                        Log.d(RESET_PASS_DEBUG, exc.message.toString())
                        Snackbar.make(requireView(), exc.message.toString(), Snackbar.LENGTH_LONG).show()
                    }
            }
        }
    }
}