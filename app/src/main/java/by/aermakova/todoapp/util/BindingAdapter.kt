package by.aermakova.todoapp.util

import android.content.res.Resources
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.db.entity.Interval
import by.aermakova.todoapp.data.remote.auth.loginManager.AppLoginManager
import by.aermakova.todoapp.ui.adapter.*
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable


@BindingAdapter("app:imageColor")
fun setImageColor(background: ImageView, status: Boolean?) {
    val res = background.context.resources
    val theme = background.context.theme
    background.background.setTint(status?.let {
        if (it) res.getColor(R.color.colorAccent, theme)
        else res.getColor(R.color.color_grey_light, theme)
    } ?: res.getColor(R.color.color_grey_light, theme))
}

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

@BindingAdapter("app:setAsDoneTemp")
fun toggleListenerKeyResult(toggleButton: ToggleButton, model: KeyResultModel?) {
    toggleButton.setOnCheckedChangeListener { _, _ ->
        model?.let {
            Log.d("A_BindingAdapter", "$model")
            model.action?.invoke(model.keyResultId, toggleButton.isChecked)
        }
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

@BindingAdapter("app:visibleSaveGoalButton", "app:visibleSaveKeyResButton")
fun setSaveGoalButtonVisibility(
    view: View,
    visibleSaveGoalButton: Boolean?,
    visibleSaveKeyResButton: Boolean?
) {
    if (visibleSaveGoalButton != null && visibleSaveKeyResButton != null)
        view.visibility =
            if (visibleSaveGoalButton || visibleSaveKeyResButton) View.VISIBLE
            else View.GONE
}

@BindingAdapter("app:onClick")
fun clickListener(view: View, listener: (() -> Unit)?) {
    view.setOnClickListener {
        listener?.invoke()
    }
}

@BindingAdapter(
    "app:onLoginClick",
    "app:addLoginMethod"
)
fun loginClickListener(
    view: View,
    listener: ((AppLoginManager) -> Unit)?,
    method: AppLoginManager?
) {
    method?.let {
        view.setOnClickListener {
            listener?.invoke(method)
        }
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

@BindingAdapter("app:itemSelectedListener")
fun setSpinnerListener(
    spinner: Spinner,
    listener: ((Long) -> Unit)?
) {
    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
            Log.d("BindingAdapter", "Nothing Selected")
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val item = parent?.selectedItem as TextModel
            listener?.invoke(item.textId)
        }
    }
}

@BindingAdapter(
    "app:addSpinnerAdapter",
    "app:disposable"
)
fun editSpinner(
    spinner: Spinner,
    itemsList: Observable<List<TextModel>>?,
    disposable: CompositeDisposable?
) {

    if (itemsList != null && disposable != null) {
        disposable.add(itemsList.subscribe(
            { list ->
                ArrayAdapter<TextModel>(
                    spinner.context,
                    R.layout.simple_spinner_item,
                    list
                ).also { adapter ->
                    adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = adapter
                }
            },
            { it.printStackTrace() }
        ))
    }
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
        val resources = flipper.context.resources
        disposable.add(status.subscribe({
            flipper.displayedChild = when (it) {
                Status.LOADING -> flipper.indexOfChild(flipper.findViewById(R.id.placeholder))
                else -> flipper.indexOfChild(flipper.findViewById(R.id.content))
            }
            if (it == Status.ERROR) {
                flipper.showSnackMessage(it.message/*resources.getString(R.string.error_while_loading)*/)
            }
        },
            {
                it.printStackTrace()
                flipper.showSnackMessage(resources.getString(R.string.error_while_loading))
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
                { updateRecyclerView(it, recyclerView) },
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
    updateRecyclerView(items, recyclerView)
}

fun updateRecyclerView(items: List<CommonModel>?, recyclerView: RecyclerView) {
    items?.let {
        val list = if (!it.isNullOrEmpty()) it else {
            listOf<CommonModel>(EmptyModel())
        }
        (recyclerView.adapter as? CommonRecyclerAdapter)?.update(list)
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