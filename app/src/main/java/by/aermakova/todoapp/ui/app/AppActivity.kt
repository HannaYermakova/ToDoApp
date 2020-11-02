package by.aermakova.todoapp.ui.app

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.remote.auth.SPLASH_AUTH_CHECK
import by.aermakova.todoapp.databinding.ActivityMainBinding
import by.aermakova.todoapp.ui.base.BaseActivity
import com.google.firebase.FirebaseApp

class AppActivity : BaseActivity<AppViewModel>() {

    private lateinit var controller: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App).tryInjectAppActivity(this)
        setTheme(R.style.Theme_AppCompat_Light_NoActionBar_FullScreen)
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val view = binding.root

        FirebaseApp.initializeApp(this)

        controller = Navigation.findNavController(this, R.id.app_host_fragment).apply {
            Navigation.setViewNavController(view, this)
        }

        if (intent.getBooleanExtra(SPLASH_AUTH_CHECK, false))
            controller.navigate(R.id.mainFlowFragment)
    }
}