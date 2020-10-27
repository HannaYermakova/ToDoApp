package by.aermakova.todoapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import by.aermakova.todoapp.util.provideClickToParent

class CommonRecyclerAdapter :
    RecyclerView.Adapter<CommonRecyclerAdapter.ViewHolder>() {

    private val listOfItems = arrayListOf<CommonModel>()

    fun update(items: List<CommonModel>) {
        val diffResult = DiffUtil.calculateDiff(CommonItemDiffUtil(listOfItems, items))
        setData(items)
        diffResult.dispatchUpdatesTo(this)
    }

    private fun setData(items: List<CommonModel>) {
        listOfItems.clear()
        listOfItems.addAll(items)
    }

    class ViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: CommonModel) {
            try { binding.setVariable(model.variableId, model) }
            catch (e: Exception) {print("The model is not attached to this layout") }

            with(binding.root) {
                setOnClickListener {
                    val func = model.clickAction
                    if (func != null) {
                        func.invoke(model.id)
                    } else {
                        provideClickToParent(it)
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return listOfItems[position].layout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding =
            DataBindingUtil.inflate(layoutInflater, viewType, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listOfItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = listOfItems[position]
        holder.bind(model)
    }
}

class CommonItemDiffUtil(
    private val oldList: List<CommonModel>,
    private val newList: List<CommonModel>
) :
    DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}