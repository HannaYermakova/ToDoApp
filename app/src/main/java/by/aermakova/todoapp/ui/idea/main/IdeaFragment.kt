package by.aermakova.todoapp.ui.idea.main

import android.os.Bundle
import androidx.lifecycle.Observer
import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.FragmentIdeasBinding
import by.aermakova.todoapp.ui.base.BaseFragment

class IdeaFragment : BaseFragment<IdeaViewModel, FragmentIdeasBinding>() {

    override val layout: Int
        get() = R.layout.fragment_ideas

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.confirmDeleteListener?.observe(viewLifecycleOwner, Observer {
            viewModel.confirmDelete(it)
        })
    }
}