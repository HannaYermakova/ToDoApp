package by.aermakova.todoapp.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.FragmentAddItemBinding
import by.aermakova.todoapp.ui.navigation.DialogNavigation
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class AddItemDialogFragment : DialogFragment(), HasSupportFragmentInjector {

    private val args: AddItemDialogFragmentArgs by navArgs()

    @Inject
    lateinit var router: DialogNavigation

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding: FragmentAddItemBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_add_item, null, false)

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder
                .setMessage(args.title)
                .setView(binding.root)
                .setPositiveButton(R.string.save_button)
                { _, _ ->
                    router.setDialogResult(binding.dialogEditText.text.toString())
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
        Log.i("AddItemDialogFragment", "injectDependencies")
        this.fragmentInjector = fragmentInjector
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return fragmentInjector
    }
}