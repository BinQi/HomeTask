package com.hometask.ext

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.google.android.material.tabs.TabLayout
import com.hometask.app.R
import com.hometask.db.entity.AccountEntity
import com.hometask.db.entity.Gender
import com.hometask.main.MainActivity
import com.hometask.repository.model.LoggedInUser

internal fun TabLayout.Tab?.toTabType(): MainActivity.TabType? {
    if (null == this) return null
    return MainActivity.TabType.fromPosition(position)
}

fun AccountEntity.toLoggedInUser(): LoggedInUser {
    return LoggedInUser(uid, nickname)
}

fun Gender.toText(context: Context): String {
    return when (this) {
        Gender.NOT_SET -> R.string.unknown
        Gender.MALE -> R.string.male
        Gender.FEMALE -> R.string.female
    }.let {
        context.resources.getString(it)
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}