package com.hometask.util

import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Copyright (C) @2021 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * @description 加密工具类
 */
object SecurityUtils {
    private const val INDEX_1 = 1
    private const val INDEX_3 = 3
    private const val HEX_FF = 0xFF
    private const val HEX_100 = 0x100
    private const val SHA1 = "SHA-1"
    private const val UTF8 = "UTF-8"

    fun sha1Hash(toHash: String): String? {
        var hash: String? = null
        try {
            val digest: MessageDigest = MessageDigest.getInstance(SHA1)
            var bytes = toHash.toByteArray(charset(UTF8))
            digest.update(bytes, 0, bytes.size)
            bytes = digest.digest()
            hash = bytesToHex(bytes)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return hash
    }

    private fun bytesToHex(bytes: ByteArray): String {
        val stringBuilder = StringBuilder()
        for (digestByte in bytes) {
            stringBuilder.append(
                Integer.toHexString(digestByte.toInt() and HEX_FF or HEX_100)
                    .substring(INDEX_1, INDEX_3)
            )
        }
        return stringBuilder.toString()
    }
}