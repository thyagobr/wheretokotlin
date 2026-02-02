package com.whereto.app.repositories

import com.whereto.app.domain.Place
import com.whereto.app.domain.Tag
import com.whereto.app.dtos.places.CreatePlaceRequest
import com.whereto.db.tables.Places
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import io.ktor.server.plugins.NotFoundException
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update

class PlaceRepository {
    fun findAll(): List<Place> = transaction {
        val places = Places.selectAll().map { row ->
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
        }

        val tagRepository = TagRepository()
        val placeIds = places.map { it.id!! }
        val tags: List<Tag> = tagRepository.findTagForPlaces(placeIds)
        val tagsByPlaceId: Map<Int, List<Tag>> = tags.groupBy { it.taggableId }

        places.map { place ->
            place.copy(tags = tagsByPlaceId[place.id!!] ?: emptyList())
        }
    }

    fun findById(id: Int): Place = transaction {
        val place: Place = Places.selectAll().where { Places.id eq id }.map { row ->
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
        }.singleOrNull() ?: throw NotFoundException("Place not found")

        val tagRepository: TagRepository = TagRepository()
        val tags = tagRepository.findTagsForPlace(id)

        place.copy(
            tags = tags
        )
    }

    fun create(placeParams: CreatePlaceRequest): Place {
        val now = Clock.System.now().toLocalDateTime(TimeZone.UTC)
        val placeId = transaction {
            val id = Places.insert {
                it[name] = placeParams.name
                it[address] = placeParams.address
                it[city] = placeParams.city
                it[country] = placeParams.country
                it[createdAt] = now
                it[updatedAt] = now
            } get Places.id
            id.value
        }

        return Place(
            id = placeId,
            name = placeParams.name,
            address = placeParams.address,
            city = placeParams.city,
            country = placeParams.country,
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
