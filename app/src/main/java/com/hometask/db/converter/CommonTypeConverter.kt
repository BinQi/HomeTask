package com.hometask.db.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hometask.db.entity.Gender

/**
 * 数据库存储 TypeConverter
 */
class CommonTypeConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromMap(map: Map<Int, String>): String {
        return gson.toJson(map)
    }

    @TypeConverter
    fun toMap(str: String): Map<Int, String> {
        val type = object : TypeToken<Map<Int, String>>() {}.type
        return gson.fromJson(str, type) ?: emptyMap()
    }

    @TypeConverter
    fun fromStrMap(map: Map<String, String>): String {
        return gson.toJson(map)
    }

    @TypeConverter
    fun toStrMap(str: String): Map<String, String> {
        val type = object : TypeToken<Map<String, String>>() {}.type
        return gson.fromJson(str, type) ?: emptyMap()
    }

    @TypeConverter
    fun fromGender(gender: Gender): Int {
        return gender.ordinal
    }

    @TypeConverter
    fun toGender(value: Int): Gender {
        return Gender.from(value)
    }

    @TypeConverter
    fun toStrList(str: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(str, type) ?: emptyList()
    }

    @TypeConverter
    fun fromStrList(strList: List<String>): String {
        return gson.toJson(strList)
    }

    @TypeConverter
    fun floatsToString(floatList: List<Float>): String {
        return gson.toJson(floatList)
    }

    @TypeConverter
    fun stringToFloatList(data: String): List<Float> {
        val type = object : TypeToken<List<Float>>() {}.type
        return gson.fromJson(data, type)
    }
}