package com.news.stackusers.feature.users.data.persistent

import androidx.room.Database
import androidx.room.RoomDatabase
import com.news.stackusers.feature.users.data.model.dto.FollowingEntity

@Database(
    entities = [FollowingEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun followingsDao(): FollowingsDao
}