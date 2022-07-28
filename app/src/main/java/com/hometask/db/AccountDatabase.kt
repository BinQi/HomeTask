package com.hometask.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hometask.db.converter.CommonTypeConverter
import com.hometask.db.dao.AccountDao
import com.hometask.db.entity.AccountEntity

/**
 * @description DB
 * 
 */
@Database(
    version = 1,
    entities = [AccountEntity::class],
)
@TypeConverters(CommonTypeConverter::class)
abstract class AccountDatabase : RoomDatabase() {

    abstract fun accountDao(): AccountDao

    companion object {
        private const val DB_NAME = "home_task"
        @Volatile
        private var instance: AccountDatabase? = null

        fun getInstance(context: Context): AccountDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AccountDatabase {
            return Room.databaseBuilder(context, AccountDatabase::class.java, DB_NAME)
                .build()
        }
    }
}