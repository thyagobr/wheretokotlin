package com.whereto.app.repositories

import com.whereto.app.domain.User
import com.whereto.db.tables.Users
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class UserRepository {
    fun findByEmail(email: String): User? = transaction {
        Users.selectAll().where(Users.email.eq(email)).map {
            User(
                id = it[Users.id].value,
                name = it[Users.name],
                email = it[Users.email],
                passwordDigest = it[Users.passwordDigest],
                token = it[Users.token],
                role = it[Users.role]

            )
        }.singleOrNull()
    }
}