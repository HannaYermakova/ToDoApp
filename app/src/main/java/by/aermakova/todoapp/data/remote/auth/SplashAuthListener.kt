package by.aermakova.todoapp.data.remote.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import by.aermakova.todoapp.ui.app.AppActivity

const val SPLASH_AUTH_CHECK = "SPLASH_AUTH_CHECK"

class SplashAuthListener(private val activity: Activity) :
    AuthListener {

    override fun isSignIn() {
        openAppActivity(true)
    }

    override fun notSignIn() {
        openAppActivity(false)
    }

    private fun openAppActivity(value: Boolean) {
        with(activity) {
            val args = Bundle()
            args.putBoolean(SPLASH_AUTH_CHECK, value)
            startActivity(Intent(this, AppActivity::class.java))
            finish()
        }
    }
}