package by.aermakova.todoapp.ui.idea.main

import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.FragmentIdeasBinding
import by.aermakova.todoapp.ui.base.BaseFragment

class IdeaFragment : BaseFragment<IdeaViewModel, FragmentIdeasBinding>() {

    override val layout: Int
        get() = R.layout.fragment_ideas

}