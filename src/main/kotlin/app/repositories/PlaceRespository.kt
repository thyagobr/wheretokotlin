package com.whereto.app.repositories

import com.whereto.app.domain.Place
import com.whereto.db.tables.Places
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class PlaceRespository {
    fun findAll(): List<Place> = transaction {
        Places.selectAll().map {
            Place(
                id = it[Places.id].value,
                name = it[Places.name],
                country = it[Places.country],
                city = it[Places.city],
            )
        }
    }
}