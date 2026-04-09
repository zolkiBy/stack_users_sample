package com.news.stackusers.feature.users.data.model.dto

import com.news.stackusers.common.net.BaseResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class UsersResponse(val items: List<UserDto>) : BaseResponse()

@Serializable
class UserDto(
    @SerialName("user_id") val userId: Int,
    val reputation: Int,
    @SerialName("profile_image") val profileImageUrl: String,
    @SerialName("display_name") val displayName: String
)