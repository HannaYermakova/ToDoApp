package by.aermakova.todoapp.util

import android.content.res.Configuration
import android.content.res.Resources
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.db.entity.Interval
import by.aermakova.todoapp.data.model.*
import by.aermakova.todoapp.data.remote.auth.loginManager.AppLoginManager
import by.aermakova.todoapp.ui.adapter.CommonRecyclerAdapter
import by.aermakova.todoapp.ui.adapter.MarginItemDecorator
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable


@BindingAdapter("app:imageColor")
fun setImageColor(background: ImageView, status: Boolean?) {
    val res = background.context.resources
    val theme = background.context.theme
    background.background.setTint(status?.let {
        if (it) res.getColor(R.color.color_scheme_shark, theme)
        else res.getColor(R.color.color_white, theme)
    } ?: res.getColor(R.color.color_white, theme))
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
                res.getColor(R.color.color_scheme_brick, theme)
            } else res.getColor(R.color.color_white, theme)
        )
        convertLongToDate(finishTime)
    } ?: res.getString(R.string.not_specified)
}

@BindingAdapter("app:intervalListener")
fun setIntervalListener(radioGroup: RadioGroup, interval: MutableLiveData<Interval>?) {
    interval?.let {
        it.postValue(
            when (radioGroup.checkedRadioButtonId) {
                R.id.daily_radio_button -> Interval.DAILY
                R.id.weekly_radio_button -> Interval.WEEKLY
                else -> Interval.MONTHLY
            }
        )
    }
}

@BindingAdapter("app:interval")
fun setInterval(radioGroup: RadioGroup, interval: Int?) {
    interval?.let {
        val selectedButton =
            when (it) {
                Interval.DAILY.code -> radioGroup.findViewById(R.id.daily_radio_button)
                Interval.WEEKLY.code -> radioGroup.findViewById(R.id.weekly_radio_button)
                else -> radioGroup.findViewById<RadioButton>(R.id.monthly_radio_button)
            }
        selectedButton.isChecked = true
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

@BindingAdapter(
    "app:visibleSaveGoalButton",
    "app:visibleSaveKeyResButton"
)
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
    "app:onClickItem",
    "app:itemId"
)
fun clickItemListener(view: View, listener: FunctionLong?, itemId: Long?) {
    view.setOnClickListener {
        itemId?.let {
            listener?.invoke(it)
        }
    }
}

@BindingAdapter(
    "app:onLongClickItem",
    "app:itemLongId"
)
fun longClickItemListener(view: View, listener: FunctionLong?, itemId: Long?) {
    view.setOnLongClickListener {
        itemId?.let {
            listener?.invoke(it)
            true
        }
        false
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
fun editTitleListener(
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

@BindingAdapter("app:tempText")
fun editTextListener(
    editText: EditText,
    tempTitle: MutableLiveData<String>?
) {
    editText.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            tempTitle?.postValue(editText.text.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    })
}

@BindingAdapter("convertLongToDate")
fun editTextListener(
    textView: TextView,
    dateText: LiveData<Long>?
) {
    textView.text =  dateText?.let {
        it.value?.let { date -> convertLongToDate(date) }
            ?: textView.context.resources.getString(R.string.pick_day)
    } ?: textView.context.resources.getString(R.string.pick_day)
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
            val pos = if (position == 0) {
                return
            } else {
                position - 1
            }
            val item = spinner.getItemAtPosition(pos) as TextModel
            listener?.invoke(item.textId)
        }
    }
}

@BindingAdapter(
    "app:addSpinnerAdapter",
    "app:addSelectedItem",
    "app:disposable",
    "app:addTitle"
)
fun editSpinner(
    spinner: Spinner,
    itemsList: Observable<List<TextModel>>?,
    selectedItem: Observable<TextModel>?,
    disposable: CompositeDisposable?,
    title: String?
) {

    if (itemsList != null && disposable != null) {
        disposable.add(itemsList.subscribe(
            { list ->
                val arrayList = arrayListOf<TextModel>()
                arrayList.add(
                    TextModel(
                        ITEM_IS_NOT_SELECTED_ID,
                        title ?: spinner.context.getString(R.string.select_item)
                    )
                )
                arrayList.addAll(list)
                SpinnerOptionalAdapter(
                    spinner.context,
                    title ?: spinner.context.getString(R.string.select_item),
                    arrayList
                ).also { adapter ->
                    spinner.adapter = adapter
                    selectedItem?.let {
                        selectedItem.subscribe({ model ->
                            spinner.setSelection(adapter.getPosition(model))
                        },
                            { it.printStackTrace() }
                        )
                    }
                }
            },
            { it.printStackTrace() }
        ))
    }
}

const val ITEM_IS_NOT_SELECTED_ID = -1L

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
                flipper.showToastMessage(it.message)
            }
        },
            {
                it.printStackTrace()
                flipper.showToastMessage(resources.getString(R.string.error_while_loading))
            }
        ))
    }
}

@BindingAdapter(
    "app:show_errors",
    "app:disposable"
)
fun setErrorMessages(
    view: View,
    status: Observable<Status>?,
    disposable: CompositeDisposable?
) {
    if (status != null && disposable != null) {
        val resources = view.context.resources
        disposable.add(status.subscribe({
            if (it == Status.ERROR) {
                view.showToastMessage(it.message)
            }
        },
            {
                it.printStackTrace()
                view.showToastMessage(resources.getString(R.string.error_while_loading))
            }
        ))
    }
}

@BindingAdapter(
    "app:bindList",
    "app:addDisposable",
    "app:itemSwipeAction"
)
fun bindCommonListToRecycler(
    recyclerView: RecyclerView,
    items: Observable<List<CommonModel>>?,
    disposable: CompositeDisposable?,
    action: FunctionLong?
) {
    if (items != null && disposable != null) {
        disposable.add(
            items.subscribe(
                { updateRecyclerView(it, recyclerView) },
                { it.printStackTrace() })
        )
    }

    action?.let {
        val touchCallback = ItemSwipeHelperCallback.Builder()
            .iconId(R.drawable.ic_delete_24)
            .backgroundColorId(R.color.color_scheme_salt_box)
            .context(recyclerView.context)
            .onSwipedAction { viewHolder, _ ->
                action.invoke(
                    recyclerView.adapter?.getItemId(viewHolder.absoluteAdapterPosition)
                        ?: throw Exception("Adapter is not attached to recycler view")
                )
            }
            .build()
        ItemTouchHelper(touchCallback).attachToRecyclerView(recyclerView)
    }
}

@BindingAdapter(
    "app:divideSize",
    "app:layoutType"
)
fun setListSettings(
    recyclerView: RecyclerView,
    divide: Int?,
    layoutManager: LayoutManagerType?
) {
    divide?.let {
        val sideMargin = when (layoutManager) {
            null -> RECYCLER_SIDE_MARGIN
            LayoutManagerType.GRID -> it
            else -> RECYCLER_SIDE_MARGIN
        }
        commonAdapterSettings(
            recyclerView,
            sideMargin,
            sideMargin,
            it,
            layoutManager
        )
    }
}

@BindingAdapter(
    "app:bindPlainList",
    "app:dividePlainSize"
)
fun bindCommonPlainListToRecycler(
    recyclerView: RecyclerView,
    items: List<CommonModel>?,
    divide: Int?
) {
    divide?.let { commonAdapterSettings(recyclerView, divide = divide) }
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
    divide: Int = 0,
    layoutType: LayoutManagerType? = null
) {
    with(recyclerView) {
        adapter = CommonRecyclerAdapter()
        if (itemDecorationCount > 0
            && ((layoutType != null && layoutType == LayoutManagerType.LINEAR_VERTICAL)
                    || layoutType == null)
        ) {
            removeItemDecorationAt(0)
        }
        addItemDecoration(
            MarginItemDecorator(
                leftMargin = leftMargin,
                rightMargin = rightMargin,
                divide = divide,
                linear = layoutType != null && layoutType == LayoutManagerType.LINEAR_VERTICAL
            )
        )
        val manager = when (layoutType) {
            null -> LinearLayoutManager(context)
            LayoutManagerType.GRID -> GridLayoutManager(
                context,
                getSpanCountByOrientation(context.resources.configuration.orientation),
                GridLayoutManager.VERTICAL,
                false
            )
            else -> LinearLayoutManager(context)
        }
        layoutManager = manager
    }
}

fun getSpanCountByOrientation(orientation: Int) =
    if (orientation == Configuration.ORIENTATION_PORTRAIT) SPAN_COUNT_PORTRAIT else SPAN_COUNT_LANDSCAPE

@BindingAdapter("app:paddingTopAndBottom")
fun setLayoutMarginTopAndBottom(view: View, value: Any?) {
    val statusBarHeight = getElementPxHeight(view.context.resources, ATTRIBUTE_NAME_STATUS_BAR)
    val navigationBarHeight =
        getElementPxHeight(view.context.resources, ATTRIBUTE_NAME_NAVIGATION_BAR)
    if (view.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        view.setPadding(0, statusBarHeight, 0, navigationBarHeight)
    } else {
        view.setPadding(navigationBarHeight, statusBarHeight, 0, 0)
    }
}

@BindingAdapter("app:paddingTop")
fun setLayoutMarginTop(view: View, value: Any?) {
    val statusBarHeight = getElementPxHeight(view.context.resources, ATTRIBUTE_NAME_STATUS_BAR)
    val navigationBarHeight =
        getElementPxHeight(view.context.resources, ATTRIBUTE_NAME_NAVIGATION_BAR)
    if (view.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        view.setPadding(0, statusBarHeight, 0, 0)
    }else {
        view.setPadding(navigationBarHeight, statusBarHeight, 0, 0)
    }
}

@BindingAdapter("app:paddingBottom")
fun setLayoutMarginBottom(view: View, value: Any?) {
    val navigationBarHeight =
        getElementPxHeight(view.context.resources, ATTRIBUTE_NAME_NAVIGATION_BAR)
    if (view.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        view.setPadding(0, 0, 0, navigationBarHeight)
    }
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
private const val RECYCLER_SIDE_MARGIN = 8
private const val SPAN_COUNT_PORTRAIT = 2
private const val SPAN_COUNT_LANDSCAPE = 4