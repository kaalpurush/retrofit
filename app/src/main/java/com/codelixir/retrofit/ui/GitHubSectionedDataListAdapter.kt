package com.codelixir.retrofit.ui

import android.view.View
import android.widget.Filter
import android.widget.Filterable
import com.codelixir.retrofit.data.GitHubEntity

class GitHubSectionedDataListAdapter(list: MutableList<GitHubEntity>) :
    BaseSectionedDataListAdapter<GitHubEntity>(list), Filterable {

    private var filteredList: MutableList<GitHubEntity> = list

    override fun onPlaceSubheaderBetweenItems(position: Int): Boolean {
        val movieGenre = filteredList[position].programing_language
        val nextMovieGenre = filteredList[position + 1].programing_language
        return movieGenre != nextMovieGenre
    }

    override fun getItemSize(): Int {
        return filteredList.size
    }

    override fun onBindItemViewHolder(holder: DataListViewHolder, position: Int) {
        val item = filteredList[position]
        holder.itemTitle.text = item.name
        holder.itemGenre.text = item.programing_language
        holder.itemView.setOnClickListener { v: View? -> listener?.onItemClicked(item) }
    }

    override fun onBindSubheaderViewHolder(
        subheaderHolder: SubheaderHolder,
        nextItemPosition: Int
    ) {
        super.onBindSubheaderViewHolder(subheaderHolder, nextItemPosition)
        val context = subheaderHolder.itemView.context
        val genre = filteredList[nextItemPosition].programing_language
        subheaderHolder.mSubheaderText.text = genre
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isBlank()) {
                    filteredList = list
                } else {
                    filteredList = list.filter { item ->
                        item.name.contains(
                            charString,
                            true
                        ) || item.programing_language.contains(
                            charString,
                            true
                        )
                    } as MutableList<GitHubEntity>
                }

                FilterResults().apply {
                    values = filteredList
                    return this
                }
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filteredList = filterResults.values as MutableList<GitHubEntity>
                notifyDataChanged()
            }
        }
    }
}