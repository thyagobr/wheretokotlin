package com.whereto.app.repositories

import com.whereto.app.domain.Place
import com.whereto.db.tables.Places
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update

class PlaceRepository {
    fun findAll(): List<Place> = transaction {
        Places.selectAll().map { row ->
            Place(
                id = row[Places.id].value,
                name = row[Places.name],
                address = row[Places.address],
                city = row[Places.city],
                country = row[Places.country],
                createdAt = row[Places.createdAt],
                updatedAt = row[Places.updatedAt],
                tags = emptyList() // filled below
            )
        }
    }

    fun findById(id: Int): Place? = transaction {
        Places.selectAll().where { Places.id eq id }.map { row ->
            Place(
                id = row[Places.id].value,
                name = row[Places.name],
                address = row[Places.address],
                city = row[Places.city],
                country = row[Places.country],
                createdAt = row[Places.createdAt],
                updatedAt = row[Places.updatedAt],
                tags = emptyList()
            )
        }.singleOrNull()
    }

    fun create(place: Place): Place {
        val now = Clock.System.now().toLocalDateTime(TimeZone.UTC)
        val placeId = transaction {
            val id = Places.insert {
                it[name] = place.name
                it[address] = place.address
                it[city] = place.city
                it[country] = place.country
                it[createdAt] = now
                it[updatedAt] = now
            } get Places.id
            id.value
        }

        return place.copy(
            id = placeId,
            createdAt = now,
            updatedAt = now
        )
    }

    fun update(place: Place): Place = transaction {
        val now = Clock.System.now().toLocalDateTime(TimeZone.UTC)
        Places.update({ Places.id eq place.id!! }) {
            it[name] = place.name
            it[address] = place.address
            it[city] = place.city
            it[country] = place.country
            it[updatedAt] = now
        }
        place.copy(updatedAt = now)
    }

    fun delete(id: Int): Boolean = transaction {
        (Places.deleteWhere { Places.id eq id } > 0)
    }
}
