package com.news.stackusers.feature.users.domain

import com.news.stackusers.common.di.IoDispatcher
import com.news.stackusers.common.domain.FlowUseCase
import com.news.stackusers.feature.users.data.UsersRepository
import com.news.stackusers.feature.users.data.model.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val usersRepository: UsersRepository,
    private val getFollowingsUseCase: GetFollowingsUseCase,
) :
    FlowUseCase<Unit, List<User>>(coroutineDispatcher) {

    override fun execute(parameters: Unit): Flow<List<User>> {
        return flow { emit(usersRepository.getUsers()) }
            .combine(getFollowingsUseCase.invoke(Unit)) { users, followings ->
                users.map { user ->
                    user.copy(isFollowing = user.id in followings)
                }
            }
    }
}