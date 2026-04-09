package com.news.stackusers.feature.users.data

import com.news.stackusers.common.DEBUG_TAG
import com.news.stackusers.common.di.IoDispatcher
import com.news.stackusers.feature.users.data.model.User
import com.news.stackusers.feature.users.data.model.dto.FollowingEntity
import com.news.stackusers.feature.users.data.model.dto.UsersResponse
import com.news.stackusers.feature.users.data.net.UserApi
import com.news.stackusers.feature.users.data.persistent.FollowingsDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsersRepositoryImpl @Inject constructor(
    private val api: UserApi,
    private val followingsDao: FollowingsDao,
    @param:IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) : UsersRepository {

    override suspend fun getUsers(): List<User> {
        Timber.tag(DEBUG_TAG).d("getUsers called")
        // uncomment this line to see Error Screen
        //throw RuntimeException()
        return withContext(coroutineDispatcher) {
            val response = api.getUsers()
            if (response.isSuccess()) {
                toUsersList(response)
            } else {
                throw RuntimeException()
            }
        }
    }

    override fun getFollowings(): Flow<Map<Int, String>> {
        return followingsDao.getFollowings()
    }

    override suspend fun deleteFollowing(userId: Int, userName: String) {
        followingsDao.deleteFollowing(FollowingEntity(userId, userName))
    }

    override suspend fun insertFollowing(userId: Int, userName: String) {
        followingsDao.insertFollowing(FollowingEntity(userId, userName))
    }

    private fun toUsersList(response: UsersResponse): List<User> {
        return response.items.map { dtoItem ->
            User(dtoItem.userId, dtoItem.displayName, dtoItem.profileImageUrl, dtoItem.reputation)
        }
    }
}