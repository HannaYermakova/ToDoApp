package by.aermakova.todoapp.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.aermakova.todoapp.R

class MainFlowFragment : Fragment() {

    private lateinit var viewModel: MainFlowViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_flow, container, false)
    }
}