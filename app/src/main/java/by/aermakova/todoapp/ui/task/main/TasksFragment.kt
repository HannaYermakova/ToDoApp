package by.aermakova.todoapp.ui.task.main

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.FragmentTasksBinding
import by.aermakova.todoapp.ui.base.BaseFragment

class TasksFragment : BaseFragment<TasksViewModel, FragmentTasksBinding>() {

    override val layout: Int
        get() = R.layout.fragment_tasks

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.confirmDeleteListener?.observe(viewLifecycleOwner, Observer {
            Log.d("A_TasksFragment", "confirmDelete $it")
            viewModel.confirmDelete(it)
        })
    }
}