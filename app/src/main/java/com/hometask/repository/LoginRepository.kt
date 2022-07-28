package com.hometask.repository

import com.hometask.repository.model.LoggedInUser

/**
 * 登录仓库
 */
class LoginRepository(val dataSource: LoginDataSource) {

    var user: LoggedInUser? = null
        private set

    init {
        user = null
    }

    fun login(username: String, password: String): Result<LoggedInUser> {
        val result = dataSource.login(username, password)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
    }
}