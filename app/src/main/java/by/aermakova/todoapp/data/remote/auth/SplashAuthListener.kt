package by.aermakova.todoapp.data.remote.auth

import android.content.Intent
import by.aermakova.todoapp.ui.app.AppActivity
import by.aermakova.todoapp.ui.splash.SplashActivity

const val SPLASH_AUTH_CHECK = "SPLASH_AUTH_CHECK"

class SplashAuthListener(
    private val activity: SplashActivity
) :
    AuthListener {

    override fun isSignIn() {
        openAppActivity(true)
    }

    override fun notSignIn() {
        openAppActivity(false)
    }

    private fun openAppActivity(value: Boolean) {
        with(activity) {
            val intent = Intent(this, AppActivity::class.java)
            intent.putExtra(SPLASH_AUTH_CHECK, value)
            startActivity(intent)
            finish()
        }
    }
}