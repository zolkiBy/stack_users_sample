package com.news.stackusers.feature.users.domain

import com.news.stackusers.common.di.IoDispatcher
import com.news.stackusers.common.domain.FlowUseCase
import com.news.stackusers.feature.users.data.UsersRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFollowingsUseCase @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val usersRepository: UsersRepository,
) :
    FlowUseCase<Unit, Map<Int, String>>(coroutineDispatcher) {

    override fun execute(parameters: Unit): Flow<Map<Int, String>> {
        return usersRepository.getFollowings()
    }
}