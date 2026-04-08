package com.news.stackusers.feature.users.data.model.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "followings")
data class FollowingEntity(
    @PrimaryKey val userId: Int,
    val name: String
)