package by.aermakova.todoapp.ui.dialog.convertIdea

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.FragmentConvertIdeaIntoTaskBinding
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ConvertIdeaIntoTaskDialogFragment : DialogFragment(), HasSupportFragmentInjector {

    @Inject
    lateinit var viewModel: ConvertIdeaIntoTaskViewModel

    private lateinit var binding: FragmentConvertIdeaIntoTaskBinding

    private val bindingViewModelId: Int = by.aermakova.todoapp.BR.viewModel

    private val disposable = CompositeDisposable()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        AndroidSupportInjection.inject(this)
        binding.setVariable(bindingViewModelId, viewModel)
        setDismissListener()
        setPickDateObserver()
    }

    private fun setDismissListener() {
        disposable.add(viewModel.dismissCommand.subscribe { if (it) dismiss() })
    }

    private fun setPickDateObserver() {
        viewModel.taskCreator.selectedFinishDateObserver?.observe(this, Observer {
            viewModel.taskCreator.checkAndSetFinishTime(it)
        })
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_convert_idea_into_task,
            null,
            false
        )
        binding.lifecycleOwner = this
        return activity?.let {
            AlertDialog.Builder(it)
                .setView(binding.root)
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private var fragmentInjector: DispatchingAndroidInjector<Fragment>? = null

    @Inject
    fun injectDependencies(fragmentInjector: DispatchingAndroidInjector<Fragment>) {
        this.fragmentInjector = fragmentInjector
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return fragmentInjector
    }

    override fun onDestroyView() {
        disposable.clear()
        super.onDestroyView()
    }
}