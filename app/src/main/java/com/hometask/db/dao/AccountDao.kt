package com.hometask.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hometask.db.entity.AccountEntity

/**
 * @description
 * 
 * @date 2022/7/27 3:53 下午
 */
@Dao
interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entities: List<AccountEntity>)

    @Delete
    fun delete(entities: List<AccountEntity>)

    @Update
    fun update(entity: AccountEntity)

    @Query("SELECT * FROM account WHERE uid = :uid")
    fun queryByUid(uid: String): List<AccountEntity>

    @Query("SELECT * FROM account WHERE active = :active")
    fun queryActiveAccount(active: Boolean = true): List<AccountEntity>
}