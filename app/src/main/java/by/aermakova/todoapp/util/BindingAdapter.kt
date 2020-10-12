package by.aermakova.todoapp.util

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("app:onClick")
fun clickListener(view: View, listener: (() -> Unit)?) {
    view.setOnClickListener {
        listener?.invoke()
    }
}