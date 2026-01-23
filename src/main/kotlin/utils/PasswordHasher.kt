package com.whereto.utils

import org.mindrot.jbcrypt.BCrypt

object PasswordHasher {

    private const val WORK_FACTOR = 12

    fun hash(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt(WORK_FACTOR))
    }

    fun verify(password: String, passwordDigest: String) : Boolean {
        return BCrypt.checkpw(password, passwordDigest)
    }
}