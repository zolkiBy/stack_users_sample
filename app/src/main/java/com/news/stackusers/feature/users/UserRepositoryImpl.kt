package com.news.stackusers.feature.users

import com.news.stackusers.feature.users.data.UserRepository
import com.news.stackusers.feature.users.data.model.User
import com.news.stackusers.feature.users.data.net.UserApi
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val api: UserApi) : UserRepository {

    override suspend fun getUsers(): List<User> {

        TODO("Not yet implemented")
    }
}