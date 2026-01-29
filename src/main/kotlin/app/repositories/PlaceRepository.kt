package com.whereto.app.repositories

import com.whereto.app.domain.Place
import com.whereto.db.tables.Places
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update

class PlaceRepository {
    fun findAll(): List<Place> = transaction {
        Places.selectAll().map {
            Place(
                id = it[Places.id].value,
                name = it[Places.name],
                address = it[Places.address],
                city = it[Places.city],
                country = it[Places.country],
                createdAt = it[Places.createdAt],
                updatedAt = it[Places.updatedAt]
            )
        }
    }

    fun findById(id: Int): Place? = transaction {
        Places.selectAll().where { Places.id eq id }.map {
            Place(
                id = it[Places.id].value,
                name = it[Places.name],
                address = it[Places.address],
                city = it[Places.city],
                country = it[Places.country],
                createdAt = it[Places.createdAt],
                updatedAt = it[Places.updatedAt]
            )
        }.singleOrNull()
    }

    fun create(place: Place): Place {
        val now = Clock.System.now().toLocalDateTime(TimeZone.UTC)
        var id = transaction {
            Places.insert {
                it[name] = place.name
                it[address] = place.address
                it[city] = place.city
                it[country] = place.country
                it[createdAt] = now
                it[updatedAt] = now
            } get Places.id
        }

        return place.copy(
            id = id.value,
            createdAt = now,
            updatedAt = now
        )
    }

    fun update(place: Place): Place = transaction {
        val now = Clock.System.now().toLocalDateTime(TimeZone.UTC)
        Places.update({ Places.id eq place.id }) {
            it[name] = place.name
            it[address] = place.address
            it[city] = place.city
            it[country] = place.country
            it[updatedAt] = now
        }

        place.copy(updatedAt = now)
    }
}
