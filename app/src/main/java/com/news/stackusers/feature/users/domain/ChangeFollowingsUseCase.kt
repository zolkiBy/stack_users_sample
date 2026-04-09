package com.news.stackusers.feature.users.domain

import com.news.stackusers.common.di.IoDispatcher
import com.news.stackusers.common.domain.UseCase
import com.news.stackusers.feature.users.data.UsersRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import javax.inject.Inject

class ChangeFollowingsUseCase @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val usersRepository: UsersRepository,
) : UseCase<ChangeFollowingsUseCase.Params, Unit>(coroutineDispatcher) {

    override suspend fun execute(parameters: Params) {
        // 0.5s delay to simulate network request
        delay(500)
        if (parameters.startFollowing) {
            usersRepository.insertFollowing(parameters.userId, parameters.userName)
        } else {
            usersRepository.deleteFollowing(parameters.userId, parameters.userName)
        }
    }

    data class Params(val startFollowing: Boolean, val userId: Int, val userName: String)
}