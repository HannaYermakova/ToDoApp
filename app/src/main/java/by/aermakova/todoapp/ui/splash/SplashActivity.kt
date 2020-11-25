package by.aermakova.todoapp.ui.splash

import android.os.Bundle
import by.aermakova.todoapp.R
import by.aermakova.todoapp.ui.app.App
import by.aermakova.todoapp.ui.base.BaseActivity

class SplashActivity : BaseActivity<SplashViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App).tryInjectSplashActivity(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

}