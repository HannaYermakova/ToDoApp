package by.aermakova.todoapp.util

import android.view.View
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