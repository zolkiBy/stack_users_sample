package com.news.stackusers.feature.users.data.model

data class User(
    private val id: Int,
    private val name: String,
    private val profileImageUrl: String,
    private val reputation: String,
    private val isFollowing: Boolean = false,
)