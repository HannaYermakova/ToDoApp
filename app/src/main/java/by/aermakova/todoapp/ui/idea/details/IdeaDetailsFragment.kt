package by.aermakova.todoapp.ui.idea.details

import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.FragmentIdeaDetailsBinding
import by.aermakova.todoapp.ui.base.BaseFragment

class IdeaDetailsFragment : BaseFragment<IdeaDetailsViewModel, FragmentIdeaDetailsBinding>() {

    override val layout: Int
        get() = R.layout.fragment_idea_details
}