package com.codelixir.retrofit.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose

@Entity()
data class GitHubEntityOwner(
    @Expose
    @PrimaryKey
    val id: Long,
    @Expose
    val login: String,
    @Expose
    val url: String,
) : BaseEntity()