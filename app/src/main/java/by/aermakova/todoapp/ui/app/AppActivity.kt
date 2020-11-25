package by.aermakova.todoapp.ui.app

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.remote.auth.LoginAuthorizationListener
import by.aermakova.todoapp.data.remote.auth.SPLASH_AUTH_CHECK
import by.aermakova.todoapp.databinding.ActivityMainBinding
import by.aermakova.todoapp.ui.base.BaseActivity
import com.google.firebase.FirebaseApp
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class AppActivity : BaseActivity<AppViewModel>() {

    private lateinit var controller: NavController

    @Inject
    lateinit var disposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App).tryInjectAppActivity(this)
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        val view = binding.root

        FirebaseApp.initializeApp(this)

        controller = Navigation.findNavController(this, R.id.app_host_fragment).apply {
            Navigation.setViewNavController(view, this)
        }

        if (intent.getBooleanExtra(SPLASH_AUTH_CHECK, true)) {
            try {
                controller.navigate(R.id.action_loginFragment_to_mainFlowFragment)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }
}