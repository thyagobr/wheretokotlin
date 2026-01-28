package com.whereto.app.repositories

import com.whereto.app.domain.Place
import com.whereto.app.dtos.CreatePlaceRequest
import com.whereto.db.tables.Places
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class PlaceRespository {
    fun findAll(): List<Place> = transaction {
        Places.selectAll().map {
            Place(
                id = it[Places.id].value,
                name = it[Places.name],
                address = it[Places.address],
                city = it[Places.city],
                country = it[Places.country],
            )
        }
    }

    fun create(place: Place): Place {
        var id = transaction {
            Places.insert {
                it[name] = place.name
                it[address] = place.address
                it[city] = place.city
                it[country] = place.country
            } get Places.id
        }

        return place.copy(id = id.value)
    }
}
