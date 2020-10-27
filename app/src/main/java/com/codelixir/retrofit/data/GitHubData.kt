package com.codelixir.retrofit.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity()
data class GitHubData(@PrimaryKey val id: Long, val name: String, val url: String)