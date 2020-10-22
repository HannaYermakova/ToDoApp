package by.aermakova.todoapp.ui.dialog.convertIdea

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.FragmentConvertIdeaIntoTaskBinding
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class ConvertIdeaIntoTaskDialogFragment : DialogFragment(), HasSupportFragmentInjector {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding: FragmentConvertIdeaIntoTaskBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_convert_idea_into_task, null, false)

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
}