package com.codelixir.retrofit.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class GenericListAdapter<T : Any>(
    @IdRes val layoutId: Int,
    private inline val bind: (item: T, holder: BaseViewHolder, itemCount: Int) -> Unit,
    private inline val clickListener: ((item: T, isLongClick: Boolean) -> Unit)? = null
) : ListAdapter<T, BaseViewHolder>(BaseItemCallback<T>()) {
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        bind(getItem(position), holder, itemCount)
        holder.itemView.setOnClickListener {
            clickListener?.invoke(getItem(position), false)
        }
        holder.itemView.setOnLongClickListener {
            clickListener?.invoke(getItem(position), true)
            true
        }
    }

    override fun getItemViewType(position: Int) = layoutId
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val root = LayoutInflater.from(parent.context).inflate(
            viewType, parent, false
        )
        return BaseViewHolder(root as ViewGroup)
    }

    override fun getItemCount() = currentList.size

}

class BaseViewHolder(container: ViewGroup) : RecyclerView.ViewHolder(container)

class BaseItemCallback<T : Any> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T) = oldItem.toString() == newItem.toString()

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T) = oldItem == newItem
}