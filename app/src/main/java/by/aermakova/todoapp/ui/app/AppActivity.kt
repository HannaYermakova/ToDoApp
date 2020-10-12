package by.aermakova.todoapp.ui.app

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.ActivityMainBinding

class AppActivity : AppCompatActivity() {

    private val viewModel: AppViewModel by viewModels()
    private lateinit var controller: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        val view = binding.root

        controller = Navigation.findNavController(this, R.id.app_host_fragment).apply {
            Navigation.setViewNavController(view, this)
        }

        viewModel.checkLogin(controller)
    }
}