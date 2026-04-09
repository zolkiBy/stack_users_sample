package com.news.stackusers.feature.users.domain

import com.news.stackusers.feature.users.data.UsersRepository
import com.news.stackusers.feature.users.data.model.User
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetUsersUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()

    private val usersRepository: UsersRepository = mockk()
    private val getFollowingsUseCase: GetFollowingsUseCase = mockk()

    private lateinit var sut: GetUsersUseCase

    private val users = listOf(
        User(
            id = ID_USER_ONE,
            name = NAME_USER_ONE,
            profileImageUrl = "https://www.gravatar.com/avatar/6d8ebb117e8d83d74ea95fbdd0f87e13?s=256&d=identicon&r=PG",
            reputation = 100,
            isFollowing = false
        ),
        User(
            id = ID_USER_TWO,
            name = NAME_USER_TWO,
            profileImageUrl = "https://www.gravatar.com/avatar/e514b017977ebf742a418cac697d8996?s=256&d=identicon&r=PG",
            reputation = 200,
            isFollowing = false
        )
    )

    @Before
    fun `set Up`() {
        sut = GetUsersUseCase(
            coroutineDispatcher = testDispatcher,
            usersRepository = usersRepository,
            getFollowingsUseCase = getFollowingsUseCase
        )
    }

    @Test
    fun `should return 2 users and first one should be following when repository returns 2 users and getFollowingsUseCase returns 1 following`() =
        runTest(testDispatcher) {
            // Given
            coEvery { usersRepository.getUsers() } returns users
            every { getFollowingsUseCase.invoke(Unit) } returns flowOf(mapOf(ID_USER_ONE to NAME_USER_ONE))

            // When
            val result = sut.invoke(Unit).first()

            // Then
            assertEquals(2, result.size)
            assertTrue(result.find { it.id == ID_USER_ONE }?.isFollowing == true)
            assertTrue(result.find { it.id == ID_USER_TWO }?.isFollowing == false)
        }

    @Test
    fun `should return 2 users and all shouldn't be following when repository returns 2 users and getFollowingsUseCase returns empty map`() =
        runTest(testDispatcher) {
            coEvery { usersRepository.getUsers() } returns users
            every { getFollowingsUseCase.invoke(Unit) } returns flowOf(emptyMap())

            val result = sut.invoke(Unit).first()

            assertEquals(2, result.size)
            assertTrue(result.find { it.id == ID_USER_ONE }?.isFollowing == false)
            assertTrue(result.find { it.id == ID_USER_TWO }?.isFollowing == false)
        }

    @Test
    fun `should return 2 users and all shouldn't be following when repository returns 2 users and getFollowingsUseCase returns 1 following but with different ids`() =
        runTest(testDispatcher) {
            coEvery { usersRepository.getUsers() } returns users
            every { getFollowingsUseCase.invoke(Unit) } returns flowOf(mapOf(3 to NAME_USER_ONE))

            val result = sut.invoke(Unit).first()

            assertEquals(2, result.size)
            assertTrue(result.find { it.id == ID_USER_ONE }?.isFollowing == false)
            assertTrue(result.find { it.id == ID_USER_TWO }?.isFollowing == false)
        }

    @Test
    fun `should return empty list when repository is empty`() =
        runTest(testDispatcher) {
            coEvery { usersRepository.getUsers() } returns emptyList()
            every { getFollowingsUseCase.invoke(Unit) } returns flowOf(emptyMap())

            val result = sut.invoke(Unit).first()

            assertTrue(result.isEmpty())
        }

    @Test(expected = RuntimeException::class)
    fun `should throw RuntimeException when repository throws exception`() =
        runTest(testDispatcher) {
            coEvery { usersRepository.getUsers() } throws RuntimeException()

            sut.invoke(Unit).first()
        }

    companion object {
        private const val ID_USER_ONE = 1
        private const val ID_USER_TWO = 2
        private const val NAME_USER_ONE = "Jon Skeet"
        private const val NAME_USER_TWO = "Gordon Linoff"
    }
}
