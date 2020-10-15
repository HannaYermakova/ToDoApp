package by.aermakova.todoapp.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class CustomRecyclerAdapter<Type> :
    RecyclerView.Adapter<CustomRecyclerAdapter.ViewHolder<Type>>() {

    private val listOfItems = arrayListOf<ModelWrapper<Type>>()

    fun update(items: List<ModelWrapper<Type>>) {
        val diffResult = DiffUtil.calculateDiff(ItemDiffUtil(listOfItems, items))
        setData(items)
        diffResult.dispatchUpdatesTo(this)
    }

    private fun setData(items: List<ModelWrapper<Type>>) {
        listOfItems.clear()
        listOfItems.addAll(items)
    }

    class ViewHolder<Type>(
        private val binding: ViewDataBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ModelWrapper<Type>) {
            try {
                binding.setVariable(model.variableId, model.type)
            } catch (e: Exception) {
                print("The model is not attached to this layout")
            }
            model.clickAction?.let { func ->
                binding.root.setOnClickListener {
                    func.invoke(model.id)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return listOfItems[position].layout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<Type> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding =
            DataBindingUtil.inflate(layoutInflater, viewType, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listOfItems.size

    override fun onBindViewHolder(holder: ViewHolder<Type>, position: Int) {
        val model = listOfItems[position]
        holder.bind(model)
    }
}

class ItemDiffUtil<Type>(
    private val oldList: List<ModelWrapper<Type>>,
    private val newList: List<ModelWrapper<Type>>
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