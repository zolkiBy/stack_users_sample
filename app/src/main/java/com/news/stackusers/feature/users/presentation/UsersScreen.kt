package com.news.stackusers.feature.users.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.news.stackusers.R
import com.news.stackusers.common.DEBUG_TAG
import com.news.stackusers.feature.users.data.model.User
import timber.log.Timber

@Composable
fun UsersScreen(
    modifier: Modifier,
    viewModel: UsersViewModel,
    onFollowButtonClicked: (startFollowing: Boolean, user: User) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    when (uiState) {
        is UsersViewModel.UiState.Error -> ErrorStateScreen(modifier)
        is UsersViewModel.UiState.Loading -> LoadingStateScreen(modifier)
        is UsersViewModel.UiState.Success -> SuccessStateScreen(
            modifier,
            (uiState as UsersViewModel.UiState.Success).users,
            onFollowButtonClicked
        )

        is UsersViewModel.UiState.ChangingFollowingsState -> SuccessStateScreen(
            modifier,
            (uiState as UsersViewModel.UiState.ChangingFollowingsState).users,
            onFollowButtonClicked
        )
    }
}

@Composable
fun ErrorStateScreen(modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(stringResource(R.string.screen_users_general_error))
    }
}

@Composable
fun LoadingStateScreen(modifier: Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun SuccessStateScreen(
    modifier: Modifier,
    users: List<User>,
    onFollowButtonClicked: (startFollowing: Boolean, user: User) -> Unit
) {
    SideEffect { Timber.tag(DEBUG_TAG).d("SuccessStateScreen, users: $users") }
    UsersList(modifier, users, onFollowButtonClicked)
}

@Composable
fun UsersList(
    modifier: Modifier,
    users: List<User>,
    onFollowButtonClicked: (startFollowing: Boolean, user: User) -> Unit
) {
    LazyColumn(modifier = modifier, contentPadding = PaddingValues(16.dp)) {
        items(items = users, key = { it.id }) { user ->
            UserDataCell(user, onFollowButtonClicked)
        }
    }
}

@Composable
fun UserDataCell(
    user: User,
    onFollowButtonClicked: (startFollowing: Boolean, user: User) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        UserProfileImage(
            imageUrl = user.profileImageUrl,
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = user.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "Reputation: ${user.reputation}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        FollowButton(
            isFollowing = user.isFollowing,
            onClick = { onFollowButtonClicked(!user.isFollowing, user) }
        )
    }
}

@Composable
fun UserProfileImage(
    imageUrl: String,
) {

    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
fun FollowButton(
    isFollowing: Boolean,
    onClick: () -> Unit,
) {
    val containerColor = if (isFollowing) {
        MaterialTheme.colorScheme.primary
    } else {
        Color.Transparent
    }

    val contentColor = if (isFollowing) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.primary
    }

    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.height(36.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
    ) {
        Text(
            text = if (isFollowing) {
                stringResource(R.string.screen_users_follow_button_following)
            } else {
                stringResource(R.string.screen_users_follow_button_not_following)
            },
            style = MaterialTheme.typography.labelMedium
        )
    }
}

