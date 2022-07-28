package com.hometask.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hometask.db.entity.AccountEntity
import com.hometask.db.entity.Gender
import com.hometask.repository.AccountRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @description
 */
class ProfileViewModel : ViewModel() {

    fun onCreate() {
        AccountRepository.initData()
    }

    fun getAccountLiveData() = AccountRepository.accountLiveData

    fun isLoggedIn(): Boolean = AccountRepository.accountLiveData.value != null

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            AccountRepository.logout()
        }
    }

    fun updateAvatar(imgPath: String) {
        AccountRepository.accountLiveData.value?.takeIf {
            it.avatarPath != imgPath
        }?.let {
            updateAccount(it.copy(avatarPath = imgPath))
        }
    }

    fun updateNickname(nickname: String) {
        AccountRepository.accountLiveData.value?.takeIf {
            it.nickname != nickname
        }?.let {
            updateAccount(it.copy(nickname = nickname))
        }
    }

    fun updateGender(gender: Gender) {
        AccountRepository.accountLiveData.value?.takeIf {
            it.gender != gender
        }?.let {
            updateAccount(it.copy(gender = gender))
        }
    }

    private fun updateAccount(entity: AccountEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            AccountRepository.updateAccount(entity)
        }
    }
}