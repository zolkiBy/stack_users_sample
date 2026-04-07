package com.news.stackusers.feature.users.data.net

import com.news.stackusers.feature.users.data.model.dto.UsersResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApi {

    @GET("users")
    suspend fun getUsers(
        @Query("page") page: Int = 1,
        @Query("pagesize") pageSize: Int = 20,
        @Query("order") order: String = "desc",
        @Query("sort") sort: String = "reputation",
        @Query("site") site: String = "stackoverflow",
    ): UsersResponse
}