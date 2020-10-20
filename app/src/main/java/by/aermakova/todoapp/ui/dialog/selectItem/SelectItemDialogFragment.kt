package by.aermakova.todoapp.ui.dialog.selectItem

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import by.aermakova.todoapp.BR
import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.FragmentSelectItemBinding
import by.aermakova.todoapp.util.adapterSettings
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

private const val SIDE_MARGIN = 8
private const val DIVIDE_MARGIN = 4

abstract class SelectItemDialogFragment<Type> : DialogFragment(), HasSupportFragmentInjector {

//    private val router: DialogNavigation<Long>

    private val viewModel: SelectItemViewModel<Type>
        get() = injectViewModel()

    private val bindingViewModelId: Int = BR.viewModel

    private lateinit var binding: FragmentSelectItemBinding

    private val title: String
        get() = setTitle()

    abstract fun injectViewModel(): SelectItemViewModel<Type>

    abstract fun setTitle(): String

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        AndroidSupportInjection.inject(this)
        binding.setVariable(bindingViewModelId, viewModel)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_select_item, null, false)
        binding.lifecycleOwner = this

        adapterSettings<Type>(binding.recyclerView, SIDE_MARGIN, SIDE_MARGIN, DIVIDE_MARGIN)

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder
                .setMessage(title)
                .setView(binding.root)
                .setPositiveButton(R.string.save_button)
                { _, _ ->
//                    router.setDialogResult(binding.dialogEditText.text.toString())
                    dismiss()
                }
                .setNegativeButton(R.string.cancel)
                { _, _ -> dismiss() }
            builder.create()
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