package by.aermakova.todoapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        Navigation.findNavController(this, R.id.main_host_fragment).apply {
            Navigation.setViewNavController(view, this)
        }
    }
}