package com.news.stackusers.feature.users.domain

import com.news.stackusers.common.di.IoDispatcher
import com.news.stackusers.common.domain.FlowUseCase
import com.news.stackusers.feature.users.data.UsersRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChangeFollowingsUseCase @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val usersRepository: UsersRepository,
) : FlowUseCase<ChangeFollowingsUseCase.Params, Unit>(coroutineDispatcher) {

    override fun execute(parameters: Params): Flow<Unit> {
        return flow {
            if (parameters.startFollowing) {
                usersRepository.insertFollowing(parameters.userId, parameters.userName)
            } else {
                usersRepository.deleteFollowing(parameters.userId, parameters.userName)
            }
            emit(Unit)
        }
    }

    class Params(val startFollowing: Boolean, val userId: Int, val userName: String)
}