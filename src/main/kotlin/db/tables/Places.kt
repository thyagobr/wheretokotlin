package com.whereto.db.tables

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

object Places: IntIdTable("places") {
    val name = varchar("name", 255)
    val address = varchar("address", 255)
    val country = varchar("country", 255)
    val city = varchar("city", 255)
}