package com.hometask.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.hometask.app.R
import com.hometask.repository.LoginRepository
import com.hometask.repository.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {
    companion object {
        private const val PWD_MIN_LENGTH = 5
        private const val EMAIL_INFIX = '@'
    }

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = loginRepository.login(username, password)

            withContext(Dispatchers.Main) {
                if (result is Result.Success) {
                    _loginResult.value =
                        LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
                } else {
                    _loginResult.value = LoginResult(error = R.string.login_failed)
                }
            }
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains(EMAIL_INFIX)) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > PWD_MIN_LENGTH
    }
}