package com.whereto.app.repositories

import com.whereto.app.domain.User
import com.whereto.db.tables.Users
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class UserRepository {
    fun findByEmail(email: String): User? = transaction {
        Users.selectAll().where(Users.email.eq(email)).map { toUser(it) }.singleOrNull()
    }

    fun findByToken(token: String): User? = transaction {
        Users.selectAll().where(Users.token.eq(token)).map { toUser(it) }.singleOrNull()
    }

    private fun toUser(row: ResultRow): User = transaction {
        User(
            id = row[Users.id].value,
            name = row[Users.name],
            email = row[Users.email],
            passwordDigest = row[Users.passwordDigest],
            token = row[Users.token],
            role = row[Users.role]
        )
    }
}