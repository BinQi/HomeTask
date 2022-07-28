package com.hometask.db.entity

/**
 * 
 *
 * @description
 * 
 * @date 2022/7/27 3:41 下午
 */
enum class Gender {
    NOT_SET,
    MALE,
    FEMALE;

    companion object {
        fun from(value: Int): Gender {
            return values().first { it.ordinal == value }
        }
    }
}