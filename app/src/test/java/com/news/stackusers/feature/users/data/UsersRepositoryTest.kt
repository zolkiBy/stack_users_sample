package com.news.stackusers.feature.users.data

import com.news.stackusers.feature.users.data.model.dto.UserDto
import com.news.stackusers.feature.users.data.model.dto.UsersResponse
import com.news.stackusers.feature.users.data.net.UserApi
import com.news.stackusers.feature.users.data.persistent.FollowingsDao
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UsersRepositoryTest {

    private val testDispatcher = StandardTestDispatcher()

    private val followingsDao: FollowingsDao = mockk()
    private val usersApi: UserApi = mockk()

    private lateinit var sut: UsersRepository

    private val usersResponse = UsersResponse(
        items = listOf(
            UserDto(
                userId = 1,
                reputation = 1500,
                profileImageUrl = "https://i.pravatar.cc/150?img=1",
                displayName = "John Doe"
            ),
            UserDto(
                userId = 2,
                reputation = 8200,
                profileImageUrl = "https://i.pravatar.cc/150?img=2",
                displayName = "Jane Smith"
            ),
            UserDto(
                userId = 3,
                reputation = 320,
                profileImageUrl = "https://i.pravatar.cc/150?img=3",
                displayName = "Bob Johnson"
            )
        )
    )

    private val errorResponse = UsersResponse(emptyList()).apply {
        errorId = 400
        description = "A malformed parameter was passed"
        errorName = "bad_parameter"
    }

    @Before
    fun `set Up`() {
        sut = UsersRepositoryImpl(
            api = usersApi,
            followingsDao = followingsDao,
            coroutineDispatcher = testDispatcher
        )
    }

    @Test
    fun `should return 2 users when api response contains 2 items`() =
        runTest(testDispatcher) {
            // Given
            coEvery { usersApi.getUsers() } returns usersResponse

            // When
            val result = sut.getUsers()

            // Then
            assertEquals(3, result.size)
            assertTrue(result[0].id == 1)
            assertTrue(result[1].id == 2)
        }

    @Test(expected = RuntimeException::class)
    fun `should throw exception when api response contains error`() =
        runTest(testDispatcher) {
            coEvery { usersApi.getUsers() } returns errorResponse

            sut.getUsers()
        }
}