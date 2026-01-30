package com.whereto.app.repositories

import com.whereto.db.tables.Events
import com.whereto.app.domain.Event
import com.whereto.db.tables.Places
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.core.eq

class EventRepository {
    fun findAllByPlaceId(placeId: Int): List<Event> = transaction {
        Events.selectAll()
            .where { Events.place eq EntityID(placeId, Places) }
            .map { row -> buildEventFromRow(row) }
    }

    fun findAll(): List<Event> = transaction {
        Events.selectAll().map { row -> buildEventFromRow(row) }
    }

    fun findById(id: Int): Event? = transaction {
        Events
            .selectAll()
            .where { Events.id eq id }
            .map { row -> buildEventFromRow(row) }
            .singleOrNull()
    }

    private fun buildEventFromRow(row: ResultRow): Event {
        return Event(
            id = row[Events.id],
            name = row[Events.name],
            place = row[Events.place].value,
            userId = row[Events.userId],
            description = row[Events.description],
            public = row[Events.public],
            link = row[Events.link],
            startsAt = row[Events.startsAt],
            endsAt = row[Events.endsAt]
        )
    }
}
