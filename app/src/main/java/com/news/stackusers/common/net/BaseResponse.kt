package com.news.stackusers.common.net

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
open class BaseResponse {

    @SerialName("error_id")
    var errorId: Int? = null

    var description: String? = null

    @SerialName("error_name")
    var errorName: String? = null

    fun isSuccess(): Boolean {
        return errorId == null
    }

    fun isError(): Boolean {
        return errorId != null
    }
}