package by.aermakova.todoapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class CustomRecyclerAdapter<Type, Binding : ViewDataBinding>(
    private val layout: Int,
    private val variableId: Int
) :
    RecyclerView.Adapter<CustomRecyclerAdapter.ViewHolder<Type, Binding>>() {

    private val listOfItems = arrayListOf<Model<Type>>()

    fun update(items: List<Model<Type>>) {
        val diffResult = DiffUtil.calculateDiff(ItemDiffUtil(listOfItems, items))
        setData(items)
        diffResult.dispatchUpdatesTo(this)
    }

    private fun setData(items: List<Model<Type>>) {
        listOfItems.clear()
        listOfItems.addAll(items)
    }

    class ViewHolder<Type, Binding : ViewDataBinding>(
        private val binding: Binding,
        private val variableId: Int
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: Model<Type>) {
            try {
                binding.setVariable(variableId, model.type)
            } catch (e: Exception) {
                print("The model is not attached to this layout")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<Type, Binding> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: Binding = DataBindingUtil.inflate(layoutInflater, layout, parent, false)
        return ViewHolder(binding, variableId)
    }

    override fun getItemCount(): Int = listOfItems.size

    override fun onBindViewHolder(holder: ViewHolder<Type, Binding>, position: Int) {
        val model = listOfItems[position]
        holder.bind(model)
    }
}

class ItemDiffUtil<Type>(
    private val oldList: List<Model<Type>>,
    private val newList: List<Model<Type>>
) :
    DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].hashCode() == newList[newItemPosition].hashCode()
    }
}

open class Model<Type>(val id: Int, val type: Type)

fun <Type> toModel(id: Int, type: Type): Model<Type> = Model(id, type)