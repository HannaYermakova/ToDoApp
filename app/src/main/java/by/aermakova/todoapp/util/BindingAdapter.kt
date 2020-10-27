package by.aermakova.todoapp.util

import android.content.res.Resources
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.db.entity.Interval
import by.aermakova.todoapp.ui.adapter.CommonModel
import by.aermakova.todoapp.ui.adapter.CommonRecyclerAdapter
import by.aermakova.todoapp.ui.adapter.MarginItemDecorator
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable

@BindingAdapter("app:selectedItem")
fun setSelectedItem(background: View, selected: Boolean?) {
    val res = background.context.resources
    val theme = background.context.theme
    background.setBackgroundColor(selected?.let {
        if (it) res.getColor(R.color.color_blue_light, theme)
        else res.getColor(R.color.color_white, theme)
    } ?: res.getColor(R.color.color_white, theme))
}

@BindingAdapter("app:setStatus")
fun setStatus(textView: TextView, status: Boolean?) {
    val res = textView.context.resources
    textView.text = status?.let {
        if (it) res.getString(R.string.status_done)
        else res.getString(R.string.status_in_progress)
    } ?: res.getString(R.string.status_in_progress)
}

@BindingAdapter("app:setDate")
fun setDate(textView: TextView, finishTime: Long?) {
    val theme = textView.context.theme
    val res = textView.context.resources
    textView.text = finishTime?.let {
        textView.setTextColor(
            if (it > System.currentTimeMillis()) {
                res.getColor(R.color.colorAccent, theme)
            } else res.getColor(R.color.color_black, theme)
        )
        convertLongToDate(finishTime)
    } ?: res.getString(R.string.not_specified)
}

@BindingAdapter("app:intervalListener")
fun setIntervalListener(radioGroup: RadioGroup, interval: MutableLiveData<Interval>?) {
    interval?.let {
        it.postValue(
            when (radioGroup.id) {
                R.id.daily_radio_button -> Interval.DAILY
                R.id.weekly_radio_button -> Interval.WEEKLY
                else -> Interval.MONTHLY
            }
        )
    }
}

@BindingAdapter("app:setScheduled")
fun setScheduled(textView: TextView, interval: Int?) {
    val res = textView.context.resources
    textView.text = interval?.let {
        when (it) {
            Interval.DAILY.code -> res.getString(R.string.daily_text)
            Interval.WEEKLY.code -> res.getString(R.string.weekly_text)
            else -> res.getString(R.string.monthly_text)
        }
    } ?: res.getString(R.string.no_text)
}

@BindingAdapter("app:showView")
fun switchView(view: ViewSwitcher, visible: Boolean?) {
    visible?.let {
        if (it) view.showNext()
    }
}

@BindingAdapter("app:checkedListener")
fun toggleListener(toggleButton: ToggleButton, checked: MutableLiveData<Boolean>?) {
    checked?.let {
        checked.postValue(toggleButton.isChecked)
    }
}

@BindingAdapter("app:visible")
fun setVisibility(view: View, visible: Boolean?) {
    visible?.let {
        view.visibility = if (it) View.VISIBLE else View.GONE
    }
}

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

@BindingAdapter("app:tempTitle")
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
    "app:show_child",
    "app:disposable"
)
fun setChildOfViewFlipper(
    flipper: ViewFlipper,
    status: Observable<Status>?,
    disposable: CompositeDisposable?
) {
    if (status != null && disposable != null) {
        val context = flipper.context
        disposable.add(status.subscribe({
            flipper.displayedChild = when (it) {
                Status.LOADING -> flipper.indexOfChild(flipper.findViewById(R.id.placeholder))
                else -> flipper.indexOfChild(flipper.findViewById(R.id.content))
            }
            if (it == Status.ERROR) {
                flipper.showSnackMessage(context.resources.getString(R.string.error_while_loading))
            }
        },
            {
                it.printStackTrace()
                flipper.showSnackMessage(context.resources.getString(R.string.error_while_loading))
            }
        ))
    }
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

@BindingAdapter("app:bindPlainList")
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
    with(recyclerView) {
        adapter = CommonRecyclerAdapter()

        if (itemDecorationCount > 0) {
            removeItemDecorationAt(0)
        }
        addItemDecoration(
            MarginItemDecorator(
                leftMargin = leftMargin,
                rightMargin = rightMargin,
                divide = divide
            )
        )
        val manager = LinearLayoutManager(context)
        layoutManager = manager
    }
}

@BindingAdapter("app:paddingTopAndBottom")
fun setLayoutMarginTopAndBottom(view: View, value: Any?) {
    val statusBarHeight = getElementPxHeight(view.context.resources, ATTRIBUTE_NAME_STATUS_BAR)
    val navigationBarHeight =
        getElementPxHeight(view.context.resources, ATTRIBUTE_NAME_NAVIGATION_BAR)
    view.setPadding(0, statusBarHeight, 0, navigationBarHeight)
}

@BindingAdapter("app:paddingTop")
fun setLayoutMarginTop(view: View, value: Any?) {
    val statusBarHeight = getElementPxHeight(view.context.resources, ATTRIBUTE_NAME_STATUS_BAR)
    view.setPadding(0, statusBarHeight, 0, 0)
}

fun getElementPxHeight(resources: Resources, identifier: String): Int {
    val statusBarId: Int? =
        resources.getIdentifier(
            identifier,
            ATTRIBUTE_DIMEN,
            ATTRIBUTE_DEF_PACKAGE
        )
    return if (statusBarId != null && statusBarId > 0) resources.getDimensionPixelSize(statusBarId) else 0
}

private const val ATTRIBUTE_NAME_STATUS_BAR = "status_bar_height"
private const val ATTRIBUTE_NAME_NAVIGATION_BAR = "navigation_bar_height"
private const val ATTRIBUTE_DIMEN = "dimen"
private const val ATTRIBUTE_DEF_PACKAGE = "android"