package com.codelixir.retrofit.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codelixir.retrofit.data.GitHubEntity
import com.codelixir.retrofit.R
import com.codelixir.retrofit.databinding.RowGithubDataBinding

class GitHubDataListAdapter :
    ListAdapter<GitHubEntity, GitHubDataListAdapter.GitHubDataListAdapterViewHolder>(
        GitHubDataDiffCallback
    ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GitHubDataListAdapterViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.row_github_data, parent, false)
        return GitHubDataListAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: GitHubDataListAdapterViewHolder, position: Int) {
        with(holder.binding) {
            val item = getItem(position)
            tvName.text = item.name
            tvUrl.text = item.url
            tvOwner.text = item.owner?.login
        }
    }

    inner class GitHubDataListAdapterViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val binding: RowGithubDataBinding = RowGithubDataBinding.bind(v)
    }

    object GitHubDataDiffCallback : DiffUtil.ItemCallback<GitHubEntity>() {
        override fun areItemsTheSame(oldItem: GitHubEntity, newItem: GitHubEntity) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: GitHubEntity, newItem: GitHubEntity) =
            oldItem == newItem
    }
}


