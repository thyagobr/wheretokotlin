package com.whereto.app.repositories

import com.whereto.db.tables.Events
import com.whereto.app.domain.Event
import com.whereto.app.domain.Place
import com.whereto.app.dtos.events.CreateEventRequest
import com.whereto.app.dtos.events.UpdateEventRequest
import com.whereto.db.tables.Places
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.update

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

    fun update(id: Int, params: UpdateEventRequest): Event? = transaction {
        val now = Clock.System.now().toLocalDateTime(TimeZone.UTC)
        val updatedRows = Events.update(
            where = { Events.id eq id }
        ) {
            params.name?.let { value -> it[Events.name] = value }
            params.description?.let { value -> it[Events.description] = value }
            //params.link?.let { value -> it[Events.link] = value }
            params.public?.let { value -> it[Events.public] = value }
            params.startsAt?.let { value -> it[Events.startsAt] = value }
            params.endsAt?.let { value -> it[Events.endsAt] = value }
            it[updatedAt] = now
        }

        if (updatedRows == 0) null else findById(id)
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
