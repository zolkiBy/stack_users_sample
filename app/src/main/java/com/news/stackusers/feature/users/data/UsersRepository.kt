package com.news.stackusers.feature.users.data

import com.news.stackusers.feature.users.data.model.User
import kotlinx.coroutines.flow.Flow

interface UsersRepository {
    suspend fun getUsers(): List<User>

    fun getFollowings(): Flow<Map<Int, String>>

    suspend fun deleteFollowing(userId: Int, userName: String)

    suspend fun insertFollowing(userId: Int, userName: String)
}