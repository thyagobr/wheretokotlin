package com.whereto.app.repositories

import com.whereto.db.tables.Events
import com.whereto.app.domain.Event
import com.whereto.app.domain.Place
import com.whereto.app.dtos.PlaceResponse
import com.whereto.db.tables.Places
import com.whereto.utils.LocalDateTimeSerializer
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.internal.throwMissingFieldException
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select

class EventRepository {
    fun findAllByPlaceId(placeId: Int): List<Event> = transaction {
        (Events innerJoin Places)
            .select(Events.columns + Places.columns)
            .where {
                Events.placeId eq placeId
            }.map { buildEventFromRow(it) }
    }

    fun findAll(): List<Event> = transaction {
        (Events innerJoin Places)
            .select(Events.columns + Places.columns)
            .map { buildEventFromRow(it) }
    }

    fun findById(id: Int): Event? = transaction {
        (Events innerJoin Places)
            .select(Events.columns + Places.columns)
            .where { Events.id eq id }
            .map { row -> buildEventFromRow(row) }
            .singleOrNull()
    }

    @Serializable
    data class CreateEventRequest(
        val name: String,
        val placeId: Int,
        val description: String,
        val link: String? = null,
        val public: Boolean = false,
        val userId: Int? = null,
        @Serializable(with = LocalDateTimeSerializer::class)
        val startsAt: LocalDateTime,
        @Serializable(with = LocalDateTimeSerializer::class)
        val endsAt: LocalDateTime?
    )

    fun create(eventRequest: CreateEventRequest): Event? = transaction {
        if (eventRequest.userId == null) {
            null
        } else {
            val now = Clock.System.now().toLocalDateTime(TimeZone.UTC)
            val id = Events.insert {
                it[Events.placeId] = eventRequest.placeId
                it[name] = eventRequest.name
                it[description] = eventRequest.description
                it[link] = eventRequest.link
                it[public] = eventRequest.public
                it[userId] = eventRequest.userId
                it[startsAt] = eventRequest.startsAt
                it[endsAt] = eventRequest.endsAt
                it[createdAt] = now
                it[updatedAt] = now
            } get Events.id

            Event(
                id = id,
                name = eventRequest.name,
                userId = eventRequest.userId,
                description = eventRequest.description,
                link = eventRequest.link,
                public = eventRequest.public,
                startsAt = eventRequest.startsAt,
                endsAt = eventRequest.endsAt,
                createdAt = now,
                updatedAt = now
            )
        }
    }

    private fun buildEventFromRow(row: ResultRow): Event {
        println("Row: $row")
        val place =
            if (row.hasValue(Places.id)) {
                Place(
                    id = row[Places.id].value,
                    name = row[Places.name],
                    address = row[Places.address],
                    city = row[Places.city],
                    country = row[Places.country]
                )
            } else {
                null
            }

        return Event(
            id = row[Events.id],
            name = row[Events.name],
            userId = row[Events.userId],
            description = row[Events.description],
            public = row[Events.public],
            link = row[Events.link],
            startsAt = row[Events.startsAt],
            endsAt = row[Events.endsAt],
            place = place,
        )
    }
}
