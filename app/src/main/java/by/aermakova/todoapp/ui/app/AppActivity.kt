package by.aermakova.todoapp.ui.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.ActivityMainBinding
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class AppActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var viewModel: AppViewModel

    private var fragmentInjector: DispatchingAndroidInjector<Fragment>? = null

    private lateinit var controller: NavController

    @Inject
    fun injectDependencies(fragmentInjector: DispatchingAndroidInjector<Fragment>) {
        this.fragmentInjector = fragmentInjector
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App).tryInjectActivity(this)
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        val view = binding.root

        controller = Navigation.findNavController(this, R.id.app_host_fragment).apply {
            Navigation.setViewNavController(view, this)
        }

        viewModel.checkLogin(controller)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? = fragmentInjector
}