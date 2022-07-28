package com.hometask.repository

import com.hometask.repository.model.LoggedInUser

class LoginDataSource {

    fun login(username: String, password: String): Result<LoggedInUser> {
        return AccountRepository.login(username, password)
    }
}