package com.krygodev.safenotes.fragments

import android.content.Intent
import androidx.fragment.app.Fragment
import com.krygodev.safenotes.activities.HomeScreenActivity
import com.krygodev.safenotes.activities.StartupActivity


// Fragment sharing methods to inheriting fragments
abstract class BaseFragment: Fragment(){

    protected fun startApp() {
        val intent = Intent(requireContext(), HomeScreenActivity::class.java).apply {
            flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        startActivity(intent)
    }

    protected fun restartApp() {
        val intent = Intent(requireContext(), StartupActivity::class.java).apply {
            flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        startActivity(intent)
    }
}