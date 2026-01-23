package com.example.db.tables

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.datetime


object Events : Table("events") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 250)
    val startsAt = datetime("start_at")
    val endsAt = datetime("end_at").nullable()

    override val primaryKey = PrimaryKey(id)
}
