package by.aermakova.todoapp.util

import android.content.Context
import android.view.ContextThemeWrapper
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.model.TextModel


class SpinnerOptionalAdapter(
    context: Context,
    private val title: String,
    list: List<TextModel>
) :
    ArrayAdapter<TextModel>(context, R.layout.simple_spinner_item, list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return if (position == 0) {
            initSelection(false)
        } else
            getCustomView(position, convertView)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return if (position == 0) {
            initSelection(true)
        } else
            getCustomView(position, convertView)
    }

    private fun initSelection(dropdown: Boolean): View {
        val view = createTextView()
        if (dropdown) {
            view.height = 0
        }
        view.text = title
        return view
    }

    private fun getCustomView(
        position: Int,
        convertView: View?
    ): View {

        val row =
            if (convertView != null && convertView !is TextView) convertView else
                createTextView()

        val pos = position - 1
        val item: TextModel? = getItem(pos)
        (row as? TextView)?.text = item.toString()
        return row
    }

    private fun createTextView(): TextView {
        val row = TextView(ContextThemeWrapper(context, R.style.spinnerItemStyle))
        val spacing = context.resources.getDimensionPixelSize(R.dimen.spacing_smaller)
        row.setPadding(spacing, spacing, 0, spacing)
        return row
    }

    override fun getCount(): Int {
        return super.getCount() + 1
    }

    override fun getPosition(item: TextModel?): Int {
        return super.getPosition(item) + 1
    }
}