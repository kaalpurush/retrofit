package com.codelixir.retrofit.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.codelixir.retrofit.R
import com.codelixir.retrofit.ui.BaseSectionedDataListAdapter.DataListViewHolder
import com.codelixir.retrofit.ui.BaseSectionedDataListAdapter.SubheaderHolder
import com.zhukic.sectionedrecyclerview.SectionedRecyclerViewAdapter

abstract class BaseSectionedDataListAdapter<T> internal constructor(var list: MutableList<T>) :
    SectionedRecyclerViewAdapter<SubheaderHolder, DataListViewHolder>() {

    interface OnItemClickListener {
        fun onItemClicked(DataList: Any?)
        fun onSubheaderClicked(position: Int)
    }

    var listener: OnItemClickListener? = null

    class SubheaderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @JvmField
        var mSubheaderText: TextView
        var mArrow: ImageView

        init {
            mSubheaderText = itemView.findViewById<View>(R.id.subheaderText) as TextView
            mArrow = itemView.findViewById<View>(R.id.arrow) as ImageView
        }
    }

    class DataListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @JvmField
        var itemTitle: TextView

        @JvmField
        var itemGenre: TextView

        init {
            itemTitle = itemView.findViewById<View>(R.id.itemTitle) as TextView
            itemGenre = itemView.findViewById<View>(R.id.itemGenre) as TextView
        }
    }

    override fun onCreateItemViewHolder(parent: ViewGroup, viewType: Int): DataListViewHolder {
        return DataListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_item, parent, false)
        )
    }

    override fun onCreateSubheaderViewHolder(parent: ViewGroup, viewType: Int): SubheaderHolder {
        return SubheaderHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_item_header, parent, false)
        )
    }

    @CallSuper
    override fun onBindSubheaderViewHolder(
        subheaderHolder: SubheaderHolder,
        nextItemPosition: Int
    ) {
        val isSectionExpanded = isSectionExpanded(getSectionIndex(subheaderHolder.adapterPosition))
        if (isSectionExpanded) {
            subheaderHolder.mArrow.setImageDrawable(
                ContextCompat.getDrawable(
                    subheaderHolder.itemView.context,
                    android.R.drawable.ic_input_add
                )
            )
        } else {
            subheaderHolder.mArrow.setImageDrawable(
                ContextCompat.getDrawable(
                    subheaderHolder.itemView.context,
                    android.R.drawable.ic_input_delete
                )
            )
        }
        subheaderHolder.itemView.setOnClickListener { v: View? ->
            listener?.onSubheaderClicked(
                subheaderHolder.adapterPosition
            )
        }
    }



    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        this.listener = onItemClickListener
    }

}