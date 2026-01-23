package com.whereto.app.repositories

import com.whereto.db.tables.Events
import com.whereto.app.domain.Event
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.core.eq

class EventRepository {

    fun findAll(): List<Event> = transaction {
        Events.selectAll().map { row ->
            Event(
                id = row[Events.id],
                name = row[Events.name],
                startsAt = row[Events.startsAt],
                endsAt = row[Events.endsAt]
            )
        }
    }

    fun findById(id: Int): Event? = transaction {
        Events
            .selectAll()
            .where { Events.id eq id }
            .map { row ->
                Event(
                    row[Events.id],
                    row[Events.name],
                    row[Events.startsAt],
                    row[Events.endsAt]
                )
            }
            .singleOrNull()
    }
}
