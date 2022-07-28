package com.hometask.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hometask.app.HomeTaskApplication
import com.hometask.db.AccountDatabase
import com.hometask.db.entity.AccountEntity
import com.hometask.ext.toLoggedInUser
import com.hometask.repository.model.LoggedInUser
import com.hometask.util.SecurityUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.security.InvalidParameterException

/**
 * @description 账户仓库管理
 */
object AccountRepository {

    private val dao by lazy {
        AccountDatabase.getInstance(HomeTaskApplication.globalContext).accountDao()
    }

    private val _accountLiveData by lazy { MutableLiveData<AccountEntity>() }
    val accountLiveData: LiveData<AccountEntity?>
        get() {
            return _accountLiveData
        }

    fun initData() {
        GlobalScope.launch(Dispatchers.IO) {
            dao.queryActiveAccount().firstOrNull()?.let {
                _accountLiveData.postValue(it)
            }
        }
    }

    @WorkerThread
    fun login(uid: String, pwd: String): Result<LoggedInUser> {
        val accounts = dao.queryByUid(uid)
        val pwSha = SecurityUtils.sha1Hash(pwd).orEmpty()
        val targetAccount = accounts.firstOrNull { it.pwSha == pwSha }
        return if (accounts.isEmpty()) {
            val entity = AccountEntity(uid, pwSha, true)
            dao.insert(listOf(entity))
            _accountLiveData.postValue(entity)
            return Result.Success(entity.toLoggedInUser())
        } else if (targetAccount != null) {
            val entity = targetAccount.copy(active = true)
            if (targetAccount.active.not()) {
                dao.update(entity)
            }
            _accountLiveData.postValue(entity)
            return Result.Success(entity.toLoggedInUser())
        } else {
            Result.Error(InvalidParameterException("password is invalid"))
        }
    }

    @WorkerThread
    fun logout() {
        _accountLiveData.value?.let {
            dao.update(it.copy(active = false))
            _accountLiveData.postValue(null)
        }
    }

    @WorkerThread
    fun updateAccount(entity: AccountEntity) {
        _accountLiveData.value?.takeIf {
            it.uid == entity.uid && it != entity
        }?.let {
            dao.update(entity)
            _accountLiveData.postValue(if (entity.active) entity else null)
        }
    }
}