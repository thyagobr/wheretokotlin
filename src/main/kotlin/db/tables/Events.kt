package com.whereto.db.tables

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.datetime


object Events : Table("events") {
    val id = integer("id").autoIncrement()
    val place = reference(
        name = "place_id",
        foreign = Places,
    )
    val name = varchar("name", 250)
    val description = varchar("description", 250)
    val link = varchar("link", 255).nullable()
    val public = bool("public")
    // TODO: make a reference
    val userId = integer("user_id")
    val startsAt = datetime("start_at")
    val endsAt = datetime("end_at").nullable()
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")

    override val primaryKey = PrimaryKey(id)
}
