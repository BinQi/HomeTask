package com.hometask.db.entity

import androidx.room.Entity

/**
 * 
 *
 * @description 账户table
 * 
 * @date 2022/7/27 3:38 下午
 */
@Entity(tableName = "account", primaryKeys = ["uid"])
data class AccountEntity(
    val uid: String,
    val pwSha: String,
    val active: Boolean,
    val avatarPath: String = "",
    val nickname: String = uid,
    val gender: Gender = Gender.NOT_SET,
)
