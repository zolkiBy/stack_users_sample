package com.news.stackusers.feature.users.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.news.stackusers.common.DEBUG_TAG
import com.news.stackusers.feature.users.data.model.User
import com.news.stackusers.feature.users.domain.ChangeFollowingsUseCase
import com.news.stackusers.feature.users.domain.GetUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    getUsersUseCase: GetUsersUseCase,
    private val changeFollowingsUseCase: ChangeFollowingsUseCase,
) : ViewModel() {

    private var currentUsers: List<User>? = null
    private val eventListener = MutableSharedFlow<Event>()
    private val usersEvent: StateFlow<Event> = getUsersUseCase.invoke(Unit)
        .map<List<User>, Event> { users ->
            Timber.tag(DEBUG_TAG).d("UsersUseCase triggered, users: $users")
            currentUsers = users
            Event.OnDataLoadedSuccessfully(users)
        }.catch { throwable ->
            Timber.tag(DEBUG_TAG).d("Error when loading users from network, throwable: $throwable")
            emit(Event.OnError(throwable))
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(STOP_TIMEOUT_IN_MILLIS),
            Event.OnLoading
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<UiState> =
        merge(eventListener, usersEvent)
            .flatMapLatest { event ->
                Timber.tag(DEBUG_TAG).d("Event received: $event")

                return@flatMapLatest when (event) {
                    is Event.OnLoading -> emitUiState(UiState.Loading)
                    is Event.OnDataLoadedSuccessfully -> emitUiState(UiState.Success(event.users))
                    is Event.OnError -> emitUiState(UiState.Error(event.exception))
                    is Event.OnFollowButtonClicked -> handleFollowClick(
                        event.startFollowing,
                        event.user
                    )
                }
            }.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(STOP_TIMEOUT_IN_MILLIS),
                UiState.Loading
            )


    fun onFollowUserClicked(startFollowing: Boolean, user: User) {
        Timber.tag(DEBUG_TAG).d("onFollowUserClicked, user: $user")
        emitEvent(Event.OnFollowButtonClicked(startFollowing, user))
    }

    private suspend fun handleFollowClick(startFollowing: Boolean, user: User): Flow<UiState> {
        changeFollowingsUseCase.invoke(
            ChangeFollowingsUseCase.Params(
                startFollowing,
                user.id,
                user.name
            )
        )
        val state = currentUsers?.let { users ->
            UiState.ChangingFollowingsState(users)
        } ?: UiState.Error(RuntimeException())

        return emitUiState(state)
    }

    private fun emitEvent(event: Event): Job = viewModelScope.launch {
        Timber.tag(DEBUG_TAG).d("Emitting event: $event")
        eventListener.emit(event)
    }

    private fun emitUiState(uiState: UiState): Flow<UiState> = flow { emit(uiState) }

    companion object {
        private const val STOP_TIMEOUT_IN_MILLIS: Long = 5_000
    }

    sealed class UiState {
        data class Success(val users: List<User>) : UiState()
        data class Error(val exception: Throwable) : UiState()
        object Loading : UiState()
        data class ChangingFollowingsState(val users: List<User>) : UiState()
    }

    sealed class Event {
        object OnLoading : Event()
        data class OnDataLoadedSuccessfully(val users: List<User>) : Event()
        data class OnError(val exception: Throwable): Event()
        data class OnFollowButtonClicked(val startFollowing: Boolean, val user: User) : Event()
    }
}