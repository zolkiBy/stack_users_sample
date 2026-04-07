package com.news.stackusers.feature.users.data

import com.news.stackusers.feature.users.data.model.User

interface UserRepository {
    suspend fun getUsers(): List<User>
}