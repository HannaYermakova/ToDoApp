package by.aermakova.todoapp.ui.splash

import android.os.Bundle
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.remote.auth.LoginAuthorizationListener
import by.aermakova.todoapp.ui.app.App
import by.aermakova.todoapp.ui.base.BaseActivity
import javax.inject.Inject

class SplashActivity : BaseActivity<SplashViewModel>() {

    @Inject
    lateinit var authListener: LoginAuthorizationListener

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App).tryInjectSplashActivity(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onStart() {
        super.onStart()
        authListener.registerListener()
    }

    override fun onStop() {
        super.onStop()
        authListener.unregisterListener()
    }
}