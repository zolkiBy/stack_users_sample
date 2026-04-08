package com.news.stackusers.feature.users.data.model

data class User(
    val id: Int,
    val name: String,
    val profileImageUrl: String,
    val reputation: Int,
    val isFollowing: Boolean = false,
)