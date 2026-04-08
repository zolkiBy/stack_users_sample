package com.news.stackusers.feature.users.data.persistent

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.news.stackusers.feature.users.data.model.dto.FollowingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FollowingsDao {

    @Query("SELECT userId, name FROM followings")
    fun getFollowings(): Flow<Map<Int, String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFollowing(following: FollowingEntity)

    @Delete
    fun deleteFollowing(following: FollowingEntity)
}