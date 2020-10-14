package by.aermakova.todoapp.util

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.aermakova.todoapp.BR
import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.ItemKeyResultBinding
import by.aermakova.todoapp.ui.adapter.CustomRecyclerAdapter
import by.aermakova.todoapp.ui.adapter.MarginItemDecorator
import by.aermakova.todoapp.ui.adapter.Model
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable

@BindingAdapter("app:onClick")
fun clickListener(view: View, listener: (() -> Unit)?) {
    view.setOnClickListener {
        listener?.invoke()
    }
}


@BindingAdapter(
    "app:openDialog",
    "app:addTitle"
)
fun openDialog(button: View, listener: ((String) -> Unit)?, title: String?) {
    title?.let {
        button.setOnClickListener {
            listener?.invoke(title)
        }
    }
}

@BindingAdapter(
    "app:tempTitle"
)
fun editTextListener(
    editText: EditText,
    tempTitle: Observer<String>?
) {
    editText.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            tempTitle?.onNext(editText.text.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    })
}

@BindingAdapter(
    "app:bindList",
    "app:addDisposable"
)
fun <Type> bindListToRecycler(
    recyclerView: RecyclerView,
    items: Observable<List<Model<Type>>>?,
    disposable: CompositeDisposable?
) {
    recyclerView.adapter = CustomRecyclerAdapter<Type, ItemKeyResultBinding>(
        R.layout.item_key_result,
        BR.keyResultItem
    )

    recyclerView.addItemDecoration(MarginItemDecorator(divide = 4))

    val manager = LinearLayoutManager(recyclerView.context)
    recyclerView.layoutManager = manager

    if (items != null && disposable != null) {
        disposable.add(
            items.subscribe(
                {
                    @Suppress("UNCHECKED_CAST")
                    (recyclerView.adapter as? CustomRecyclerAdapter<Type, ItemKeyResultBinding>)?.update(
                        it
                    )
                },
                { it.printStackTrace() })
        )
    }
}