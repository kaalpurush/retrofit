package com.codelixir.retrofit.data

class GitHubEntityOwnerConverter : GsonConverter<GitHubEntityOwner>() {
    override fun getClassType(): Class<GitHubEntityOwner> = GitHubEntityOwner::class.java
}