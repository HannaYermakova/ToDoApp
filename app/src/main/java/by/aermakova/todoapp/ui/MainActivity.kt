package by.aermakova.todoapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val view = binding.root
        Navigation.findNavController(this, R.id.app_host_fragment).apply {
            Navigation.setViewNavController(view, this)
        }
    }
}