package com.whereto.db.tables

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

object Users: IntIdTable("users") {
    val name = varchar("name", 50)
    val email = varchar("email", 50).uniqueIndex()
    val passwordDigest = varchar("password_digest", 50)
    val role = varchar("role", 50).nullable()
    val token = varchar("token", 50).nullable()
}