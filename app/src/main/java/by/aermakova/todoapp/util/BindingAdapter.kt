package by.aermakova.todoapp.util

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.aermakova.todoapp.ui.adapter.CommonModel
import by.aermakova.todoapp.ui.adapter.CommonRecyclerAdapter
import by.aermakova.todoapp.ui.adapter.MarginItemDecorator
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
fun bindCommonListToRecycler(
    recyclerView: RecyclerView,
    items: Observable<List<CommonModel>>?,
    disposable: CompositeDisposable?
) {
    commonAdapterSettings(recyclerView, 8, 8, 4)

    if (items != null && disposable != null) {
        disposable.add(
            items.subscribe(
                {
                    @Suppress("UNCHECKED_CAST")
                    (recyclerView.adapter as? CommonRecyclerAdapter)?.update(it)
                },
                { it.printStackTrace() })
        )
    }
}

@BindingAdapter(
    "app:bindPlainList"
)
fun bindCommonPlainListToRecycler(
    recyclerView: RecyclerView,
    items: List<CommonModel>?
) {
    commonAdapterSettings(recyclerView, divide = 2)
    @Suppress("UNCHECKED_CAST")
    items?.let {
        (recyclerView.adapter as? CommonRecyclerAdapter)?.update(it)
    }
}

fun commonAdapterSettings(
    recyclerView: RecyclerView,
    leftMargin: Int = 0,
    rightMargin: Int = 0,
    divide: Int = 0
) {
    recyclerView.adapter = CommonRecyclerAdapter()
    recyclerView.addItemDecoration(
        MarginItemDecorator(
            leftMargin = leftMargin,
            rightMargin = rightMargin,
            divide = divide
        )
    )
    val manager = LinearLayoutManager(recyclerView.context)
    recyclerView.layoutManager = manager
}