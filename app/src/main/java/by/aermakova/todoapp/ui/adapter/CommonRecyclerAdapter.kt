package by.aermakova.todoapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import by.aermakova.todoapp.data.model.CommonModel


class CommonRecyclerAdapter : RecyclerView.Adapter<CommonRecyclerAdapter.ViewHolder>() {

    private val asyncListDiffer: AsyncListDiffer<CommonModel> =
        AsyncListDiffer(this, DIFF_CALLBACK)

    fun update(items: List<CommonModel>) {
        asyncListDiffer.submitList(items)
    }

    class ViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: CommonModel) {
            try {
                binding.setVariable(model.variableId, model)
            } catch (e: Exception) {
                print("The model is not attached to this layout")
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return asyncListDiffer.currentList[position].layout
    }

    override fun getItemId(position: Int): Long {
        return asyncListDiffer.currentList[position].id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding =
            DataBindingUtil.inflate(layoutInflater, viewType, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = asyncListDiffer.currentList[position]
        holder.bind(model)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<CommonModel> =
            object : DiffUtil.ItemCallback<CommonModel>() {
                override fun areItemsTheSame(oldItem: CommonModel, newItem: CommonModel): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: CommonModel,
                    newItem: CommonModel
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}