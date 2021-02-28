package com.codelixir.retrofit.ui

import android.view.View
import com.codelixir.retrofit.data.GitHubEntity

class GitHubSectionedDataListAdapter(itemList: List<GitHubEntity>) :
    BaseSectionedDataListAdapter(itemList) {
    override fun onPlaceSubheaderBetweenItems(position: Int): Boolean {
        val movieGenre = list[position].programing_language
        val nextMovieGenre = list[position + 1].programing_language
        return movieGenre != nextMovieGenre
    }

    override fun onBindItemViewHolder(holder: DataListViewHolder, position: Int) {
        val item = list[position]
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
        val genre = list[nextItemPosition].programing_language
        subheaderHolder.mSubheaderText.text = genre
    }
}