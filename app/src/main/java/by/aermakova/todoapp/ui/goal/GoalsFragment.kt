package by.aermakova.todoapp.ui.goal

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.FragmentGoalsBinding
import by.aermakova.todoapp.ui.base.BaseFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class GoalsFragment : BaseFragment<FragmentGoalsBinding>(), HasSupportFragmentInjector {

    private var fragmentInjector: DispatchingAndroidInjector<Fragment>? = null

    @Inject
    fun injectDependencies(fragmentInjector: DispatchingAndroidInjector<Fragment>) {
        this.fragmentInjector = fragmentInjector
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return fragmentInjector
    }

    @Inject
    lateinit var viewModel: GoalsViewModel

    override val layout: Int
        get() = R.layout.fragment_goals

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onResume() {
        super.onResume()
        binding.viewModel = viewModel
        viewModel.checkGoalsViewModel()
    }
}