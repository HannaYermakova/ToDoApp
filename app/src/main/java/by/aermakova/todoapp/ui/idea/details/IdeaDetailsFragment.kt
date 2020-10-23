package by.aermakova.todoapp.ui.idea.details

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.FragmentIdeaDetailsBinding
import by.aermakova.todoapp.ui.base.BaseFragment

class IdeaDetailsFragment : BaseFragment<IdeaDetailsViewModel, FragmentIdeaDetailsBinding>() {

    override val layout: Int
        get() = R.layout.fragment_idea_details

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.convertIdeaToTaskObserver?.observe(viewLifecycleOwner, Observer {
            viewModel.saveAndClose(it)
        })
    }
}